package com.example.content2.Service.Impl;


import com.example.content2.Mapper.*;
import com.example.content2.POJO.MeasuredValue;
import com.example.content2.POJO.Region;
import com.example.content2.POJO.Result;
import com.example.content2.POJO.SuggestValue;
import com.example.content2.Service.SuggestValueService;
import com.example.content2.Util.*;
import com.example.content2.Util.Execel.GenerateSmartCard;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service("SuggestValueService")
public class SuggestValueServiceImpl implements SuggestValueService {
    private Log log = LogFactory.getLog(this.getClass());
    private SuggestValueMapper suggestValueMapper;
    private ExpertSuggestValueMapper expertSuggestValueMapper;
    private CropTypesMapper cropTypesMapper;
    private ElementMapper elementMapper;
    private RegionMapper regionMapper;
    private MeasuredValueMapper measuredValueMapper;
    private ItemProperties itemProperties;
    private GenerateSmartCard generateSmartCard;

    private ConcurrentHashMap<File,Long> timerQueue;

    @Value("${excelTemplate.serverIp}")
    private String serverIp;

    @Value("${excelTemplate.indicatedPath}")
    private String indicatedPath;

    @Autowired
    public SuggestValueServiceImpl(SuggestValueMapper suggestValueMapper, MeasuredValueMapper measuredValueMapper,
                                   ExpertSuggestValueMapper expertSuggestValueMapper, CropTypesMapper cropTypesMapper,
                                   ElementMapper elementMapper, RegionMapper regionMapper,
                                   ItemProperties itemProperties,GenerateSmartCard generateSmartCard,
                                   ConcurrentHashMap<File,Long> timerQueue) {
        this.suggestValueMapper = suggestValueMapper;
        this.expertSuggestValueMapper = expertSuggestValueMapper;
        this.cropTypesMapper = cropTypesMapper;
        this.elementMapper = elementMapper;
        this.regionMapper = regionMapper;
        this.measuredValueMapper = measuredValueMapper;
        this.itemProperties = itemProperties;
        this.generateSmartCard = generateSmartCard;
        this.timerQueue = timerQueue;
    }

    @Override
    public Double selectResult(Integer crop_type, String name_element, Double measured_value) {
        return suggestValueMapper.selectResult(crop_type, name_element, measured_value);
    }

    @Override
    public Double selectSuggestValueWithExpert(Double longitude, Double latitude, Integer crop_typeId, String name_element, Double measured_value) {
        Integer elementId = elementMapper.getElementIdByExpression(name_element);

        Double expertSuggestValue =
                expertSuggestValueMapper.getExpertSuggestValue(longitude, latitude, elementId, crop_typeId);

        return expertSuggestValue != null ? expertSuggestValue :
                suggestValueMapper.selectResult(crop_typeId, name_element, measured_value);
    }


    @Override
    public Result fun1(String longitudeText, String latitudeText, String crop_name, HashMap params) {
        Result result = null;

        if ((result = passCheckParamLegality(longitudeText, latitudeText, crop_name, params)) != null) { //检查参数合法性
            //未通过检查
            return result;
        }

        Double longitude;
        Double latitude;

        //获取 Request中的经度纬度
        latitude = Double.parseDouble(latitudeText);
        longitude = Double.parseDouble(longitudeText);

//        log.info("用户输入的longitude : " + longitudeText + ",latitude : " + latitudeText + ",用户输入的typeName : " + crop_name);


        Vector<String> names = itemProperties.getNameVector();//通过元素配置类，获取需要测量的元素名

        Integer crop_type = cropTypesMapper.getTypeIdByName(crop_name);//获得了作物id
        Region region = regionMapper.getRegionByLongitudeAndLatitude(longitude, latitude);//由经纬度查询测量点，可能为空
        HashMap resultMap = new HashMap<String, Object>();//存放显示数据的容器


        if (region != null) {//是直接测量点
            resultMap.put("isDirectMeasured", "true");

            //处理最终结果
            processResult(region, names,crop_type,resultMap);

        } else {//是间接测量点
            resultMap.put("isDirectMeasured", "false");

            //查找最近测量点 作为代替.  核心算法getMinRegion()
            Region min_region = getMinRegion(longitude, latitude);
            Double min_Longitude = min_region.getLongitude();
            Double min_Latitude = min_region.getLatitude();

            resultMap.put("min_Longitude", min_Longitude);
            resultMap.put("min_Latitude", min_Latitude);

//            log.info("最近测量点为 : " + min_region);

            //处理最终结果
            processResult(min_region, names,crop_type,resultMap);
        }
        log.info("查询出的Map ： " + resultMap);
        return Result.getInstance(200, "查询成功", resultMap);

    }

    @Override
    public ArrayList<HashMap<String, Object>> getSuggestValuesByLimit(int page, int size) {
        ArrayList<SuggestValue> allSuggestValueByLimit = suggestValueMapper.getSuggestValuesByLimit((page - 1) * size, size);
        ArrayList<HashMap<String, Object>> res = new ArrayList<>();
        for (SuggestValue suggestValue : allSuggestValueByLimit) {
            HashMap<String, Object> fieldMap = new HashMap<>();
            String cropName = cropTypesMapper.getNameByTypeId(suggestValue.getCrop_type());
            fieldMap.put("id",suggestValue.getId());
            fieldMap.put("cropName",cropName);
            fieldMap.put("name_element",suggestValue.getName_element());
            fieldMap.put("nameElement",elementMapper.getTranslationByExpression(suggestValue.getName_element()));
            fieldMap.put("min_value",suggestValue.getMin_value());
            fieldMap.put("max_value",suggestValue.getMax_value());
            fieldMap.put("result",suggestValue.getResult());
            res.add(fieldMap);
        }
        return res;
    }

    @Override
    public Result updateSuggestValue(HashMap map) {
        String[] fieldNames = new String[]{"cropName","name_element","min_value","max_value","result","id"};
        Class[] clz = new Class[]{String.class,String.class,Double.class, Double.class,Double.class,Integer.class};
        try {
            Object[] field = getFieldFromMap.getField(map, fieldNames, clz);
            SuggestValue suggestValue = new SuggestValue();
            Integer crop_type = cropTypesMapper.getTypeIdByName((String) field[0]);
            suggestValue.setCrop_type(crop_type);
            suggestValue.setName_element((String) field[1]);
            suggestValue.setMin_value((Double) field[2]);
            suggestValue.setMax_value((Double) field[3]);
            suggestValue.setResult((Double) field[4]);
            suggestValue.setId((Integer) field[5]);
            int i = suggestValueMapper.dynamicUpdateSuggestValue(suggestValue);
            if (i==1){
                return Result.getInstance(200,"修改成功!",null);
            }else {
                return Result.getInstance(501,"未知错误",map);
            }
        } catch (getFieldFromMap.notFoundSuchField e) {
            e.printStackTrace();
            return Result.getInstance(500,"参数错误",e.getMessage());
        }
    }

    @Override
    public Integer getLatestId() {
        return suggestValueMapper.getLatestId();
    }

    @Override
    public Result insertNewSuggestValue(HashMap map) {

        String[] fieldNames = new String[]{"cropName","name_element","min_value","max_value","result"};
        Class[] clz = new Class[]{String.class,String.class,Double.class, Double.class,Double.class};
        try {
            Object[] field = getFieldFromMap.getField(map, fieldNames, clz);
            for (Object o : field) {
                if (o==null){
                    return Result.getInstance(500,"参数错误",map);
                }
            }
            SuggestValue sv = new SuggestValue();
            Integer crop_type = cropTypesMapper.getTypeIdByName((String) field[0]);
            sv.setCrop_type(crop_type);
            sv.setName_element((String) field[1]);
            sv.setMin_value((Double) field[2]);
            sv.setMax_value((Double) field[3]);
            sv.setResult((Double) field[4]);
            sv.setId(this.getLatestId()+1);
            int i = suggestValueMapper.insertNewSuggestValue(sv);
            if (i==1){
                return Result.getInstance(200,"添加成功!",null);
            }else {
                return Result.getInstance(501,"未知错误",map);
            }
        } catch (getFieldFromMap.notFoundSuchField e) {
            e.printStackTrace();
            return Result.getInstance(500,"参数错误",e.getMessage());
        }

    }


    @Override
    public Result deleteSuggestValue(HashMap map) {
        try {
            Object[] field = getFieldFromMap.getField(map, new String[]{"id"}, new Class[]{Integer.class});
            if (suggestValueMapper.isExistId((Integer) field[0])==1) {//是否存在id
                int i = suggestValueMapper.deleteById((Integer) field[0]);//删除
                if (i==1){
                    return Result.getInstance(200,"删除成功",null);
                }
                return Result.getInstance(500,"删除失败",map);
            }
            return Result.getInstance(501,"不存在这样id",map);
        } catch (getFieldFromMap.notFoundSuchField notFoundSuchField) {
            notFoundSuchField.printStackTrace();
            return Result.getInstance(502,"未知错误",map);
        }
    }

    @Override
    public Integer getSuggestValueTotal() {
        return suggestValueMapper.getSuggestValuesTotal();
    }

    @Override
    public Result getExcelURl(HashMap map) {

        String[] fieldNames = new String[]{"mea_Effective_N","mea_Olsen_P","mea_Olsen_K","mea_organic_matter","mea_ph"};
        Class[] clz = new Class[]{Double.class,Double.class,Double.class,Double.class,Double.class};
        long l = System.currentTimeMillis();
        String path = indicatedPath+"/"+l+".xls";
        try {
            Object[] field = getFieldFromMap.getField(map, fieldNames, clz);

            try {
                generateSmartCard.generateSmartCard1(path,field);

            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (GenerateSmartCard.notCorrectFile | GenerateSmartCard.TemplateFileNotFound notCorrectFile) {
                notCorrectFile.printStackTrace();
                return Result.getInstance(501, notCorrectFile.getMessage(), null);
            }


        } catch (getFieldFromMap.notFoundSuchField notFoundSuchField) {
            notFoundSuchField.printStackTrace();
            return Result.getInstance(201,"参数错误 ： 找不到指定参数",map);
        }
        HashMap<Object, Object> resultMap = new HashMap<>();
        resultMap.put("url","http://"+serverIp+"/"+l+".xls");

        timerQueue.put(new File(path),l);
        return Result.getInstance(200,"生成成功!",resultMap);
     }

    /***
     *  检查fun1参数合法性
     *  检查: 字符串非空。  经纬度仅为数字
     *
     * @param longitudeText 经度字符串
     * @param latitudeText 纬度字符串
     * @param crop_name 作物名称
     * @param params 返回容器
     * @return 封装结果
     */
    private Result passCheckParamLegality(String longitudeText, String latitudeText, String crop_name, HashMap params) {
        if (!StringLengthGreaterThanZero.judge(longitudeText) || !judgeOnlyNumber.judgeOnlyNumber(longitudeText))
            return Result.getInstance(400, "经度(longitude)参数错误", params);
        if (!StringLengthGreaterThanZero.judge(latitudeText) || !judgeOnlyNumber.judgeOnlyNumber(latitudeText))
            return Result.getInstance(400, "纬度(latitude)参数错误", params);
        if (!StringLengthGreaterThanZero.judge(crop_name))
            return Result.getInstance(400, "作物(cropName)参数错误", params);
        return null;
    }

    /**
     *
     * 处理最终的结果,返回指定数据。
     *
     * 例如 ： 返回建议值，返回测量值。
     *
     * @param region
     * @param names
     * @param crop_type
     * @param resultMap
     */
    private void processResult(Region region, Vector<String> names, Integer crop_type, HashMap resultMap) {

        Long id_village = region.getId();//获取了村id
        MeasuredValue measuredValue = measuredValueMapper.selectById_village(id_village);//获取了这个村的土壤测量值

        String name_countryside = region.getName_countryside(); //镇名
        String name_village = region.getName_village();  //村名
        resultMap.put("name_countryside", name_countryside);//封装到map中
        resultMap.put("name_village", name_village);

        //获取所有已配置的 测量值
        Double[] meaValue = getMeaValue(measuredValue, itemProperties.getNameVector());

        //获取所有已配置的 建议值
        Double[] sugValue = getSugValue(meaValue, region.getLongitude(), region.getLatitude(), itemProperties.getNameVector(),crop_type);

        FormatAndOut formatAndOut = new FormatAndOut();
        formatAndOut.formatAndOut(resultMap, itemProperties.getNameVector(),meaValue,sugValue);
//
//
//
        String[] fieldNames = new String[]{
                "sug_Olsen_K","sug_Olsen_P","sug_Effective_N"};
        Class[] fieldClz = new Class[]{Double.class,Double.class,Double.class};
        Object[] field = new Object[0];
        try {
            field = getFieldFromMap.getField(resultMap, fieldNames, fieldClz);
        } catch (getFieldFromMap.notFoundSuchField notFoundSuchField) {
            notFoundSuchField.printStackTrace();
        }

        Double K2SO4 = (Double)field[0] / 0.6;

        Double KCL = (Double)field[0]/0.5;

        Double erAn = (Double) field[1]/0.46;

        Double niaoSu = ((Double) field[2]-erAn*0.16)/0.6;

        resultMap.put("sug_K2SO4", DoubleFormat.format(2,K2SO4));
        resultMap.put("sug_KCL", DoubleFormat.format(2,KCL));
        resultMap.put("sug_erAn", DoubleFormat.format(2,erAn));
        resultMap.put("sug_niaoSu", DoubleFormat.format(2,niaoSu));
    }


    /**
     * 核心算法: 获得最近测量点
     *
     * 测量点分布未知,用户输入未知
     *
     *
     * @param longitude
     * @param latitude
     * @return
     */
    private Region getMinRegion(Double longitude, Double latitude) {
        Double offset = 0.000100;//偏移量125.279689
        Double longitude_low = longitude - offset;
        Double longitude_high = longitude + offset;
        Double latitude_low = latitude - offset;
        Double latitude_high = latitude + offset;
        List<Region> regions;
        while (true) {
            regions = regionMapper.selectOffsetRegion(longitude_low, longitude_high, latitude_low, latitude_high);
            if (regions != null && regions.size() != 0) break;
            offset *= 2;
            longitude_low -= offset;
            longitude_high += offset;
            latitude_low -= offset;
            latitude_high += offset;
        }
        Double min = Double.MAX_VALUE;
        Region min_region = null;
        for (Region r : regions) {
            Double distance = Math.abs(r.getLongitude() - longitude) + Math.abs(r.getLatitude() - latitude);
            if (distance < min) {
                min = distance;
                min_region = r;
            }
        }
        return min_region;
    }

    /**
     *
     * @param source   MeasuredValue 类型的数据源，POJO形式 从数据库中查出
     * @param items    已配置的需要查询的所有元素。  在Fun1SuggestItemsConfig配置类中配置。
     * @return 返回已配置元素的数组。
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
     * 查询专家值，查询不到专家值，使用基础测量值。
     *
     * @param meaValue   所有对应元素的测量值,根据测量值计算出基础建议值。如果没有是null
     * @param longitude 经度
     * @param latitude 纬度
     * @param items 已配置元素
     * @return  最终结果值
     */
    public Double[] getSugValue(Double[] meaValue, Double longitude, Double latitude, Vector<String> items,Integer crop_type) {
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


    /**
     *
     * 内部配置类
     *
     * 详情请在 com/example/content2/Config/Fun1SuggestItemsConfig 中配置
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
}
