package com.example.content2.Service;

import com.example.content2.POJO.SoilAnalyse.Result;

import java.util.ArrayList;
import java.util.HashMap;

public interface SuggestValueService {

    /***
     *  只查询suggest_value表中的数据
     */
    Double selectResult(Integer crop_type, String name_element, Double measured_value);

    /***
     * 查询建议值，包含专家结果
     */
    Double selectSuggestValueWithExpert(Double longitude, Double latitude,
                                        Integer crop_typeId, String name_elementId, Double measured_value);

    Result fun1(String longitudeText,
                String latitudeText,
                String crop_name,
                String RemoteAddr, boolean isTourist);

    ArrayList<HashMap<String, Object>> getSuggestValuesByLimit(int page, int size);

    Result updateSuggestValue(HashMap map);

    Integer getLatestId();

    Result insertNewSuggestValue(HashMap<String, Object> map);

    Result deleteSuggestValue(HashMap<String, Object> map);

    Integer getSuggestValueTotal();

    Result getExcelURl(HashMap<String, Object> map);
}
