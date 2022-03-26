package com.example.content2.Service.fun1Calculate.Impl;

import com.example.content2.Mapper.Primary.RegionMapper;
import com.example.content2.Mapper.Secondary.CalculateMapper;
import com.example.content2.Mapper.Secondary.RecordMapper;
import com.example.content2.POJO.SoilAnalyse.MeaPoint;
import com.example.content2.POJO.SoilAnalyse.Region;
import com.example.content2.POJO.SoilAnalyse.Result;
import com.example.content2.POJO.fun1Calculate.CalculateRecord;
import com.example.content2.POJO.fun1Calculate.ResultRecord;
import com.example.content2.Service.Impl.SuggestValueServiceImpl.*;
import com.example.content2.Service.SuggestValueService;
import com.example.content2.Service.fun1Calculate.CalculateRecordService;
import com.example.content2.Service.fun1Calculate.RandomMeaPointGenerate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service("CalculateRecordService")
public class CalculateRecordServiceImpl implements CalculateRecordService {

    private static final Log log = LogFactory.getLog(CalculateRecordServiceImpl.class);

    @Resource
    private CalculateMapper calculateMapper;

    @Resource(name = "defaultRedisTemplate")
    private RedisTemplate<String,?> redisTemplate;

    @Value("${region.debug}")
    private boolean regionDebug;

    @Resource(name = "regionsCache")
    private List<Region> regionsCache;

    @Resource
    private SuggestValueService suggestValueService;

    @Resource
    private RecordMapper recordMapper;

    @Resource
    private RegionMapper regionMapper;

    @Override
    public int createTable(String tableName) {
        return calculateMapper.createCalculateTable(tableName);
    }

    @Override
    public void calculateOffsetMagnificationForData(double offset,
                                                    int magnification,
                                                    long loopTimes) {

        BigDecimal appendTimes = new BigDecimal("0.0");
        BigDecimal response = new BigDecimal("0.0");
        BigDecimal regionSize = new BigDecimal("0.0");
        Long database_date = (Long) redisTemplate.opsForValue().get("databaseVersionDate");
        Long calculate_date = System.currentTimeMillis();
        for (long i = 0; i <loopTimes; i++) {
            //生成随机测量点
            DefaultRandomMeaPointGenerator generator = new DefaultRandomMeaPointGenerator();
            MeaPoint meaPoint = generator.getMeaPoint();
            CalculateRecord record = new CalculateRecord();
            Double longitude = meaPoint.getLongitude();
            Double latitude = meaPoint.getLatitude();

            //查询
            long start = System.currentTimeMillis();

            Region region = regionMapper.getRegionByLongitudeAndLatitude(longitude,latitude);
            record.setLongitude(longitude);
            record.setLatitude(latitude);
            record.setOffset(offset);
            record.setMagnification(magnification);
            record.setIs_direct(1);
            record.setCalculate_date(calculate_date);
            record.setDatabase_date(database_date);
            if (region==null){
                MinRegionHandle handle = new MinRegionHandle(regionMapper,regionDebug,regionsCache);
                handle.setOffset(offset);
                handle.setMagnification(magnification);
                Region minRegion = handle.get(longitude, latitude);
                long end1 = System.currentTimeMillis();
                record.setAppend_time(handle.getAppendTimes());
                record.setResponse(end1-start);
                record.setIs_direct(0);
                record.setRegion_size(handle.getRegionSize());
            }
            //输出本次结果
            String tableName = getTableName();
            int j = insertNewRecordIfAbsentCreate(tableName, record);
            //统计本次结果

            appendTimes=appendTimes.add(new BigDecimal(record.getAppend_time()));
            response=response.add(new BigDecimal(record.getResponse()));
            regionSize = regionSize.add(new BigDecimal(record.getRegion_size()));
        }
        BigDecimal divAppendTimes = appendTimes.divide(new BigDecimal(loopTimes));
        BigDecimal divResponse = response.divide(new BigDecimal(loopTimes));
        BigDecimal divRegionSize = regionSize.divide(new BigDecimal(loopTimes));
        //记录统计结果
        ResultRecord resultRecord = new ResultRecord(null,
                database_date,calculate_date,offset,magnification,
                divAppendTimes.doubleValue(),divResponse.longValue()
                ,loopTimes,divRegionSize.longValue());
        recordMapper.insertNewResultRecord(resultRecord);
    }

    @Override
    public boolean isExistTable(String tableName) {
        if (tableExistCache==null)tableExistCache = new ConcurrentHashMap<>();
        boolean res = calculateMapper.showTable(tableName)!=null;
        if (res)tableExistCache.put(tableName,true);
        return res;
    }

    private ConcurrentHashMap<String,Boolean> tableExistCache;

    @Override
    public int insertNewRecordIfAbsentCreate(String tableName,CalculateRecord cr) {
        if ((tableExistCache!=null && tableExistCache.containsKey(tableName)) || isExistTable(tableName) ){
            return calculateMapper.insertNewCalculateRecord(tableName, cr);
        }
        createTable(tableName);
        return calculateMapper.insertNewCalculateRecord(tableName, cr);
    }


    @Override
    public Result calculateTemplate(Integer loopTimes,
                                    Double offset,
                                    Double offsetIncrement,
                                    Integer fori,
                                    Integer magnification) {

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new CalculateRunnable(loopTimes, offset,offsetIncrement,fori,
                magnification,this));
        return Result.getInstance(200,"成功收到任务,开始执行...",null);
    }


    private String getDatabaseDateFormat(){
        Long databaseVersionDate = (Long) redisTemplate.opsForValue().get("databaseVersionDate");
        Calendar instance = Calendar.getInstance();
        assert databaseVersionDate!=null;
        instance.setTimeInMillis(databaseVersionDate);
        return new SimpleDateFormat("yyyy_MM_dd").format(instance.getTime());
    }

    private String getCalculateDateFormat(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
        Calendar calendar =Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return sdf.format(calendar.getTime());
    }

    private String getTableName(){
        StringBuilder sb = new StringBuilder("record_");
        sb.append(getDatabaseDateFormat());
        sb.append("_");
        sb.append(getCalculateDateFormat());
        return sb.toString();
    }

    static class DefaultRandomMeaPointGenerator implements RandomMeaPointGenerate {

        private Double longitudeLow = 124.5; //经度
        private Double longitudeHigh = 125.5;
        private Double latitudeLow = 45.0; //纬度
        private Double latitudeHigh = 46.0;

        private double longitudeDiff = longitudeHigh-longitudeLow;
        private double latitudeDiff = latitudeHigh-latitudeLow;

        public DefaultRandomMeaPointGenerator() {
        }

        public DefaultRandomMeaPointGenerator(Double longitudeLow,
                                              Double longitudeHigh,
                                              Double latitudeLow,
                                              Double latitudeHigh) {
            this.longitudeLow = longitudeLow;
            this.longitudeHigh = longitudeHigh;
            this.latitudeLow = latitudeLow;
            this.latitudeHigh = latitudeHigh;
            longitudeDiff = longitudeHigh-longitudeLow;
            latitudeDiff = latitudeHigh-latitudeLow;
        }
        @Override
        public MeaPoint getMeaPoint(){
            MeaPoint MeaPoint = new MeaPoint();
            MeaPoint.setLongitude(longitudeLow+Math.random()*longitudeDiff);
            MeaPoint.setLatitude(longitudeHigh+Math.random()*latitudeDiff);
            return MeaPoint;
        }


    }


    static class CalculateRunnable implements Runnable{

        Integer loopTimes;
        Double offset;
        Double offsetIncrement;
        Integer fori;
        Integer magnification;
        CalculateRecordService service;

        public CalculateRunnable(Integer loopTimes,
                                 Double offset,
                                 Double offsetIncrement,
                                 Integer fori,
                                 Integer magnification,
                                 CalculateRecordService service) {
            this.loopTimes = loopTimes;
            this.offset = offset;
            this.offsetIncrement = offsetIncrement;
            this.fori = fori;
            this.magnification = magnification;
            this.service = service;
        }

        @Override
        public void run() {

            log.info("起始offset : "+offset);
            log.info("offset 递增量 : "+offsetIncrement);
            log.info("循环次数 : "+loopTimes);
            log.info("扩增倍数 : "+magnification);
            log.info("for循环递增次数 : "+fori);


            for (int i = 0; i <fori; i++) {
                long start = System.currentTimeMillis();
                log.info("正在测试 offset : "+offset + " magnification : "+magnification);
                service.calculateOffsetMagnificationForData(offset,magnification,loopTimes);
                long end = System.currentTimeMillis();
                System.out.println("本次测试完成,测试花费 : "+(end-start)+"ms");
                offset +=offsetIncrement;
            }
        }
    }
}
