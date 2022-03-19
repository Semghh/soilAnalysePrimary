package com.example.content2.Service;

import com.example.content2.POJO.SoilAnalyse.ExpertSuggestValue;
import com.example.content2.POJO.SoilAnalyse.Result;

import java.util.ArrayList;
import java.util.HashMap;


public interface ExpertSuggestService {

    public Double getExpertSuggestValue(Double longitude, Double latitude,
                                        Integer elementId, Integer cropTypeId);

    public int insertNewExpertSuggestValue(ExpertSuggestValue expertSuggestValue);

    public Result insertNewExpertSuggest(ExpertSuggestValue expertSuggestValue);

    public Result insertNewExpertSuggests(Double latitude, Double longitude, String cropName, HashMap<String,Double >nameAndValue );

    public boolean isExistSuggestValue(Double longitude, Double latitude, Integer elementId, Integer cropTypeId);

    public boolean isExistSuggestValueByBean(ExpertSuggestValue expertSuggestValue);

    public int updateSuggestValue(ExpertSuggestValue expertSuggestValue);

    public ArrayList<ExpertSuggestValue> getExpertSuggestValueByLimit(Integer page,Integer size);

    public int deleteById(Integer id);

    public Integer getExpertValueTotal();
}
