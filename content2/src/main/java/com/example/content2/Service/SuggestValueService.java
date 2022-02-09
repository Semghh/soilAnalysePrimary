package com.example.content2.Service;

import com.example.content2.POJO.Result;
import com.example.content2.POJO.SuggestValue;

import java.util.ArrayList;
import java.util.HashMap;

public interface SuggestValueService {

    /***
     *  只查询suggest_value表中的数据
     */
    public Double selectResult(Integer crop_type,String name_element,Double measured_value);

    /***
     * 查询建议值，包含专家结果
     */
    public Double selectSuggestValueWithExpert(Double longitude,Double latitude,
                                               Integer crop_typeId,String name_elementId,Double measured_value);

    public Result fun1(String longitudeText, String latitudeText, String crop_name, HashMap params);

    public ArrayList<HashMap<String,Object>> getSuggestValuesByLimit(int page, int size);

    public Result updateSuggestValue(HashMap map);

    public Integer getLatestId();

    public Result insertNewSuggestValue(HashMap map);

    public Result deleteSuggestValue(HashMap map);

    public Integer getSuggestValueTotal();

    public Result getExcelURl(HashMap map);
}
