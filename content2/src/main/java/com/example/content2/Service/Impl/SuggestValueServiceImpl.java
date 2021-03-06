package com.example.content2.Service.Impl;


import com.example.content2.Mapper.Primary.*;
import com.example.content2.POJO.SoilAnalyse.MeasuredValue;
import com.example.content2.POJO.SoilAnalyse.Region;
import com.example.content2.POJO.SoilAnalyse.Result;
import com.example.content2.POJO.SoilAnalyse.SuggestValue;
import com.example.content2.Service.SuggestValueService;
import com.example.content2.Util.*;
import com.example.content2.Util.Execel.GenerateSmartCard;
import lombok.Getter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service("SuggestValueService")
public class SuggestValueServiceImpl implements SuggestValueService {
    private final Log log = LogFactory.getLog(this.getClass());
    private final SuggestValueMapper suggestValueMapper;
    private final ExpertSuggestValueMapper expertSuggestValueMapper;
    private final CropTypesMapper cropTypesMapper;
    private final ElementMapper elementMapper;
    private final RegionMapper regionMapper;
    private final MeasuredValueMapper measuredValueMapper;
    private final ItemProperties itemProperties;
    private final GenerateSmartCard generateSmartCard;
    private final Collection<Fun1ResultMapHandle> chains;

    @Resource(name = "defaultRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${region.debug}")
    private boolean regionDebug;

    @Resource(name = "regionsCache")
    private List<Region> regionCache;

    private final ConcurrentHashMap<File, Long> timerQueue;

    @Value("${excelTemplate.serverIp}")
    private String serverIp;

    @Value("${excelTemplate.indicatedPath}")
    private String indicatedPath;

    @Autowired
    public SuggestValueServiceImpl(SuggestValueMapper suggestValueMapper, MeasuredValueMapper measuredValueMapper,
                                   ExpertSuggestValueMapper expertSuggestValueMapper, CropTypesMapper cropTypesMapper,
                                   ElementMapper elementMapper, RegionMapper regionMapper,
                                   ItemProperties itemProperties, GenerateSmartCard generateSmartCard,
                                   @Qualifier("defaultHandleChain") Collection<Fun1ResultMapHandle> chains,
                                   ConcurrentHashMap<File, Long> timerQueue) {
        this.suggestValueMapper = suggestValueMapper;
        this.expertSuggestValueMapper = expertSuggestValueMapper;
        this.cropTypesMapper = cropTypesMapper;
        this.elementMapper = elementMapper;
        this.regionMapper = regionMapper;
        this.measuredValueMapper = measuredValueMapper;
        this.itemProperties = itemProperties;
        this.generateSmartCard = generateSmartCard;
        this.chains = chains;
        this.timerQueue = timerQueue;
    }

    @Override
    public Double selectResult(Integer crop_type, String name_element, Double measured_value) {
        return suggestValueMapper.selectResult(crop_type, name_element, measured_value);
    }

    @Override
    public Double selectSuggestValueWithExpert(Double longitude, Double latitude,
                                               Integer crop_typeId, String name_element,
                                               Double measured_value) {
        Integer elementId = elementMapper.getElementIdByExpression(name_element);

        Double expertSuggestValue =
                expertSuggestValueMapper.getExpertSuggestValue(longitude, latitude, elementId, crop_typeId);

        return expertSuggestValue != null ? expertSuggestValue :
                suggestValueMapper.selectResult(crop_typeId, name_element, measured_value);
    }


    /**
     * fun1 ????????????????????????:
     * <p>
     * ????????????????????????->???-> ????????????
     * ||
     * V
     * ??? -> ?????????????????????????????? -> ????????????
     *
     * @param longitudeText ??????Text
     * @param latitudeText  ??????text
     * @param crop_name     ?????????
     * @param remoteAddr    ????????????
     * @param isTourist     ??????????????????
     */
    @Override
    public Result fun1(String longitudeText,
                       String latitudeText,
                       String crop_name,
                       String remoteAddr,
                       boolean isTourist) {
        Result result;


        //?????????????????????
        if ((result = passCheckParamLegality(longitudeText, latitudeText, crop_name)) != null) {
            //???????????????
            return result;
        }
        //??????????????????
        if (!checkTouristQueryTime(remoteAddr, isTourist)) {
            return Result.getInstance(210, "??????????????????????????????", null);
        }

        //?????? Request??????????????????
        Double longitude = Double.parseDouble(longitudeText);
        Double latitude = Double.parseDouble(latitudeText);
        //        log.info("???????????????longitude : " + longitudeText + ",latitude : " + latitudeText + ",???????????????typeName : " + crop_name);


        Vector<String> names = itemProperties.getNameVector();//??????????????????????????????????????????????????????
        Integer crop_type = cropTypesMapper.getTypeIdByName(crop_name);//???????????????id
        HashMap<String, Object> resultMap = new HashMap<>();//???????????????????????????


        Region region = regionMapper.getRegionByLongitudeAndLatitude(longitude, latitude);//??????????????????????????????????????????
        if (region != null) {//??????????????????
            resultMap.put("isDirectMeasured", "true");

            //??????????????????
            processResult(region, names, crop_type, resultMap, remoteAddr, isTourist);

        } else {//??????????????????
            resultMap.put("isDirectMeasured", "false");

            //????????????????????? ????????????.  ????????????getMinRegion()

            Region min_region = new MinRegionHandle(regionMapper,regionDebug,regionCache).get(longitude, latitude);
            //?????????????????????
            dealWithMinLocation(min_region.getLongitude(), min_region.getLatitude(), resultMap);
            //??????????????????
            processResult(min_region, names, crop_type, resultMap, remoteAddr, isTourist);
        }
        log.info("????????????Map ??? " + resultMap);
        return Result.getInstance(200, "????????????", resultMap);
    }

    @Override
    public ArrayList<HashMap<String, Object>> getSuggestValuesByLimit(int page, int size) {
        ArrayList<SuggestValue> allSuggestValueByLimit = suggestValueMapper.getSuggestValuesByLimit((page - 1) * size, size);
        ArrayList<HashMap<String, Object>> res = new ArrayList<>();
        for (SuggestValue suggestValue : allSuggestValueByLimit) {
            HashMap<String, Object> fieldMap = new HashMap<>();
            String cropName = cropTypesMapper.getNameByTypeId(suggestValue.getCrop_type());
            fieldMap.put("id", suggestValue.getId());
            fieldMap.put("cropName", cropName);
            fieldMap.put("name_element", suggestValue.getName_element());
            fieldMap.put("nameElement", elementMapper.getTranslationByExpression(suggestValue.getName_element()));
            fieldMap.put("min_value", suggestValue.getMin_value());
            fieldMap.put("max_value", suggestValue.getMax_value());
            fieldMap.put("result", suggestValue.getResult());
            res.add(fieldMap);
        }
        return res;
    }

    @Override
    public Result updateSuggestValue(HashMap map) {
        String[] fieldNames = new String[]{"cropName", "name_element", "min_value", "max_value", "result", "id"};
        Class<?>[] clz = new Class[]{String.class, String.class, Double.class, Double.class, Double.class, Integer.class};
        try {
            Object[] field = getFieldFromMap.get(map, fieldNames, clz);
            SuggestValue suggestValue = new SuggestValue();
            Integer crop_type = cropTypesMapper.getTypeIdByName((String) field[0]);
            suggestValue.setCrop_type(crop_type);
            suggestValue.setName_element((String) field[1]);
            suggestValue.setMin_value((Double) field[2]);
            suggestValue.setMax_value((Double) field[3]);
            suggestValue.setResult((Double) field[4]);
            suggestValue.setId((Integer) field[5]);
            int i = suggestValueMapper.dynamicUpdateSuggestValue(suggestValue);
            if (i == 1) {
                return Result.getInstance(200, "????????????!", null);
            } else {
                return Result.getInstance(501, "????????????", map);
            }
        } catch (getFieldFromMap.notFoundSuchField e) {
            e.printStackTrace();
            return Result.getInstance(500, "????????????", e.getMessage());
        }
    }

    @Override
    public Integer getLatestId() {
        return suggestValueMapper.getLatestId();
    }

    @Override
    public Result insertNewSuggestValue(HashMap<String, Object> map) {

        String[] fieldNames = new String[]{"cropName", "name_element", "min_value", "max_value", "result"};
        Class<?>[] clz = new Class[]{String.class, String.class, Double.class, Double.class, Double.class};
        try {
            Object[] field = getFieldFromMap.get(map, fieldNames, clz);
            for (Object o : field) {
                if (o == null) {
                    return Result.getInstance(500, "????????????", map);
                }
            }
            SuggestValue sv = new SuggestValue();
            Integer crop_type = cropTypesMapper.getTypeIdByName((String) field[0]);
            sv.setCrop_type(crop_type);
            sv.setName_element((String) field[1]);
            sv.setMin_value((Double) field[2]);
            sv.setMax_value((Double) field[3]);
            sv.setResult((Double) field[4]);
            sv.setId(this.getLatestId() + 1);
            int i = suggestValueMapper.insertNewSuggestValue(sv);
            if (i == 1) {
                return Result.getInstance(200, "????????????!", null);
            } else {
                return Result.getInstance(501, "????????????", map);
            }
        } catch (getFieldFromMap.notFoundSuchField e) {
            e.printStackTrace();
            return Result.getInstance(500, "????????????", e.getMessage());
        }

    }


    @Override
    public Result deleteSuggestValue(HashMap<String, Object> map) {
        try {
            Object[] field = getFieldFromMap.get(map, new String[]{"id"}, new Class[]{Integer.class});
            if (suggestValueMapper.isExistId((Integer) field[0]) == 1) {//????????????id
                int i = suggestValueMapper.deleteById((Integer) field[0]);//??????
                if (i == 1) {
                    return Result.getInstance(200, "????????????", null);
                }
                return Result.getInstance(500, "????????????", map);
            }
            return Result.getInstance(501, "???????????????id", map);
        } catch (getFieldFromMap.notFoundSuchField notFoundSuchField) {
            notFoundSuchField.printStackTrace();
            return Result.getInstance(502, "????????????", map);
        }
    }

    @Override
    public Integer getSuggestValueTotal() {
        return suggestValueMapper.getSuggestValuesTotal();
    }

    /**
     * ??????Excel???????????????
     */
    @Override
    public Result getExcelURl(HashMap<String, Object> map) {

        String[] fieldNames = new String[]{"mea_Effective_N",
                "mea_Olsen_P",
                "mea_Olsen_K",
                "mea_organic_matter",
                "mea_ph"};
        Class<?>[] clz = new Class[]{Double.class,
                Double.class,
                Double.class,
                Double.class,
                Double.class};

        long l = System.currentTimeMillis();
        String path = indicatedPath + "/" + l + ".xls";
        try {
            Object[] field = getFieldFromMap.get(map, fieldNames, clz);

            try {
                generateSmartCard.generateSmartCard1(path, field);

            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (GenerateSmartCard.notCorrectFile | GenerateSmartCard.TemplateFileNotFound notCorrectFile) {
                notCorrectFile.printStackTrace();
                return Result.getInstance(501, notCorrectFile.getMessage(), null);
            }


        } catch (getFieldFromMap.notFoundSuchField notFoundSuchField) {
            notFoundSuchField.printStackTrace();
            return Result.getInstance(201, "???????????? ??? ?????????????????????", map);
        }
        HashMap<Object, Object> resultMap = new HashMap<>();
        resultMap.put("url", "http://" + serverIp + "/" + l + ".xls");

        timerQueue.put(new File(path), l);
        return Result.getInstance(200, "????????????!", resultMap);
    }

    /***
     *  ??????fun1???????????????
     *  ??????: ??????????????????  ?????????????????????
     *
     * @param longitudeText ???????????????
     * @param latitudeText ???????????????
     * @param crop_name ????????????
     * @return ????????????
     */
    private Result passCheckParamLegality(String longitudeText, String latitudeText, String crop_name) {
        if (!StringLengthGreaterThanZero.judge(longitudeText) || !judgeOnlyNumber.judgeOnlyNumber(longitudeText))
            return Result.getInstance(400, "??????(longitude)????????????", null);
        if (!StringLengthGreaterThanZero.judge(latitudeText) || !judgeOnlyNumber.judgeOnlyNumber(latitudeText))
            return Result.getInstance(400, "??????(latitude)????????????", null);
        if (!StringLengthGreaterThanZero.judge(crop_name))
            return Result.getInstance(400, "??????(cropName)????????????", null);
        return null;
    }

    /**
     * ?????????????????????,?????????????????????
     * <p>
     * ?????? ??? ????????????????????????????????????
     *
     * @param region    ??????
     * @param names     ??????List
     * @param crop_type ????????????
     * @param resultMap ????????????
     */
    private void processResult(Region region, Vector<String> names,
                               Integer crop_type,
                               HashMap<String, Object> resultMap,
                               String remoteAddr,
                               boolean isTourist) {

        Long id_village = region.getId();//????????????id
        MeasuredValue measuredValue = measuredValueMapper.selectById_village(id_village);//????????????????????????????????????

        String name_countryside = region.getName_countryside(); //??????
        String name_village = region.getName_village();  //??????
        resultMap.put("name_countryside", name_countryside);//?????????map???
        resultMap.put("name_village", name_village);

        //???????????????????????? ?????????
        Double[] meaValue = getMeaValue(measuredValue, itemProperties.getNameVector());

        //???????????????????????? ?????????
        Double[] sugValue = getSugValue(meaValue, region.getLongitude(), region.getLatitude(), itemProperties.getNameVector(), crop_type);

        FormatAndOut formatAndOut = new FormatAndOut();
        formatAndOut.formatAndOut(resultMap, itemProperties.getNameVector(), meaValue, sugValue);


        //???????????????
        chains.forEach(x -> x.resultMapHandle(resultMap));


        dealWithTourist(remoteAddr, isTourist);
    }

    private String getTouristKey(String remoteAddr, boolean isTourist) {
        return remoteAddr + "Count";
    }

    private void dealWithTourist(String remoteAddr, boolean isTourist) {
        if (!isTourist) return;
        String key = getTouristKey(remoteAddr, isTourist);
        Object o = redisTemplate.opsForValue().get(key);
        if (o == null) {
            redisTemplate.opsForValue().set(key, 1);
        } else {
            redisTemplate.opsForValue().increment(key);
        }
    }

    private boolean checkTouristQueryTime(String remoteAddr, boolean isTourist) {
        if (!isTourist) return true;
        String key = getTouristKey(remoteAddr, isTourist);
        Object o = redisTemplate.opsForValue().get(key);
        if (o == null) return true;
        if (o instanceof Integer) {
            return (Integer) o < 3;
        } else if (o instanceof Long) {
            return (Long) o < 3;
        }
        return false;
    }

    /**
     * @param source MeasuredValue ?????????????????????POJO?????? ?????????????????????
     * @param items  ??????????????????????????????????????????  ???Fun1SuggestItemsConfig?????????????????????
     * @return ?????????????????????????????????
     */
    public Double[] getMeaValue(MeasuredValue source, Vector<String> items) {
        Double[] res = new Double[items.size()];
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals("ph")) res[i] = source.getPh();
            else if (items.get(i).equals("organic_matter")) res[i] = source.getOrganic_matter();
            else if (items.get(i).equals("total_nitrogen")) res[i] = source.getTotal_nitrogen();
            else if (items.get(i).equals("Olsen_P")) res[i] = source.getOlsen_P();
            else if (items.get(i).equals("Olsen_K")) res[i] = source.getOlsen_K();
            else if (items.get(i).equals("slowly_K")) res[i] = source.getSlowly_K();
            else if (items.get(i).equals("Effective_Cu")) res[i] = source.getEffective_Cu();
            else if (items.get(i).equals("Effective_Zn")) res[i] = source.getEffective_Zn();
            else if (items.get(i).equals("Effective_Fe")) res[i] = source.getEffective_Fe();
            else if (items.get(i).equals("Effective_Mn")) res[i] = source.getEffective_Mn();
            else if (items.get(i).equals("Effective_N")) res[i] = source.getEffective_N();
        }

        return res;
    }

    /***
     * ??????????????????????????????????????????????????????????????????
     *
     * @param meaValue   ??????????????????????????????,?????????????????????????????????????????????????????????null
     * @param longitude ??????
     * @param latitude ??????
     * @param items ???????????????
     * @return ???????????????
     */
    public Double[] getSugValue(Double[] meaValue, Double longitude, Double latitude, Vector<String> items, Integer crop_type) {
        Double[] res = new Double[items.size()];
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals("ph"))
                res[i] = this.selectSuggestValueWithExpert(longitude, latitude, crop_type, "ph", meaValue[i]);
            else if (items.get(i).equals("organic_matter"))
                res[i] = this.selectSuggestValueWithExpert(longitude, latitude, crop_type, "organic_matter", meaValue[i]);
            else if (items.get(i).equals("total_nitrogen"))
                res[i] = this.selectSuggestValueWithExpert(longitude, latitude, crop_type, "total_nitrogen", meaValue[i]);
            else if (items.get(i).equals("Olsen_P"))
                res[i] = this.selectSuggestValueWithExpert(longitude, latitude, crop_type, "Olsen_P", meaValue[i]);
            else if (items.get(i).equals("Olsen_K"))
                res[i] = this.selectSuggestValueWithExpert(longitude, latitude, crop_type, "Olsen_K", meaValue[i]);
            else if (items.get(i).equals("slowly_K"))
                res[i] = this.selectSuggestValueWithExpert(longitude, latitude, crop_type, "slowly_K", meaValue[i]);
            else if (items.get(i).equals("Effective_Cu"))
                res[i] = this.selectSuggestValueWithExpert(longitude, latitude, crop_type, "Effective_Cu", meaValue[i]);
            else if (items.get(i).equals("Effective_Zn"))
                res[i] = this.selectSuggestValueWithExpert(longitude, latitude, crop_type, "Effective_Zn", meaValue[i]);
            else if (items.get(i).equals("Effective_Fe"))
                res[i] = this.selectSuggestValueWithExpert(longitude, latitude, crop_type, "Effective_Fe", meaValue[i]);
            else if (items.get(i).equals("Effective_Mn"))
                res[i] = this.selectSuggestValueWithExpert(longitude, latitude, crop_type, "Effective_Mn", meaValue[i]);
            else if (items.get(i).equals("Effective_N"))
                res[i] = this.selectSuggestValueWithExpert(longitude, latitude, crop_type, "Effective_N", meaValue[i]);
        }
        return res;

    }


    private void dealWithMinLocation(Double min_Longitude,
                                     Double min_Latitude,
                                     HashMap<String, Object> resultMap) {
        resultMap.put("min_Longitude", min_Longitude);
        resultMap.put("min_Latitude", min_Latitude);
    }

    /**
     * ???????????????
     * <p>
     * ???????????? com/example/content2/Config/Fun1SuggestItemsConfig ?????????
     */
    public static class ItemProperties {

        public enum items {
            ph, organic_matter, total_nitrogen,
            Olsen_P, Olsen_K, slowly_K, Effective_Cu,
            Effective_Zn, Effective_Fe, Effective_Mn, Effective_N
        }


        HashSet<items> items = new HashSet<>();

        public ItemProperties addItem(items i) {
            items.add(i);
            return this;
        }

        public Vector<String> getMeaNameVector() {
            Vector<String> res = new Vector<>();
            for (items i : items) {
                res.add("mea_" + i.toString());
            }
            return res;
        }

        public Vector<String> getSugNameVector() {
            Vector<String> res = new Vector<>();
            for (items i : items) {
                res.add("sug_" + i.toString());
            }
            return res;
        }

        public Vector<String> getNameVector() {
            Vector<String> res = new Vector<>();
            for (items i : items) {
                res.add(i.toString());
            }
            return res;
        }

    }


    /**
     * ?????????,???????????????????????????
     * <p>
     * ????????????????????????????????????????????????
     */
    @Getter
    public static class MinRegionHandle {

        private final RegionMapper regionMapper;
        private boolean debug;
        private final List<Region> regionsCache;

        public MinRegionHandle(RegionMapper regionMapper,boolean debug,List<Region> regionsCache) {
            this.regionMapper = regionMapper;
            this.debug = debug;
            this.regionsCache = regionsCache;
        }

        private double offset = 0.078;
        private int magnification = 2;
        private int appendTimes = 0;
        private int regionSize = 0;
        private double longitude_low;
        private double longitude_high;
        private double latitude_low;
        private double latitude_high;

        public void setOffset(double offset) {
            this.offset = offset;
        }

        public void setMagnification(int magnification) {
            this.magnification = magnification;
        }

        public Region get(Double longitude, Double latitude){
            if (debug)return getMinRegionDebug(longitude,latitude);
            return getMinRegion(longitude,latitude);
        }

        private Region getMinRegionDebug(Double longitude, Double latitude) {
            longitude_low = longitude - offset;
            longitude_high = longitude + offset;
            latitude_low = latitude - offset;
            latitude_high = latitude + offset;
            List<Region> regions;
            appendTimes = 0;
            while (true) {
                regions = regionMapper.selectOffsetRegion(longitude_low, longitude_high, latitude_low, latitude_high);
                appendTimes++;
                if (appendTimes>=8)regions = regionsCache;
                if (regions != null && regions.size() != 0) break;
                offset *= magnification;
                longitude_low -= offset;
                longitude_high += offset;
                latitude_low -= offset;
                latitude_high += offset;
            }
            return dealWithMinRegion(longitude, latitude, regions);
        }

        private Region dealWithMinRegion(Double longitude, Double latitude, List<Region> regions) {
            double min = Double.MAX_VALUE;
            Region min_region = null;
            regionSize = regions.size();
            for (Region r : regions) {
                double distance = Math.abs(r.getLongitude() - longitude) + Math.abs(r.getLatitude() - latitude);
                if (distance < min) {
                    min = distance;
                    min_region = r;
                }
            }
            return min_region;
        }

        private Region getMinRegion(Double longitude, Double latitude) {
            double longitude_low = longitude - offset;
            double longitude_high = longitude + offset;
            double latitude_low = latitude - offset;
            double latitude_high = latitude + offset;
            List<Region> regions;
            while (true) {
                regions = regionMapper.selectOffsetRegion(longitude_low, longitude_high, latitude_low, latitude_high);
                if (regions != null && regions.size() != 0) break;
                offset *= magnification;
                longitude_low -= offset;
                longitude_high += offset;
                latitude_low -= offset;
                latitude_high += offset;
            }
            return dealWithMinRegion(longitude, latitude, regions);
        }
    }
}
