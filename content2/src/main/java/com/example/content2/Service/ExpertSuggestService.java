package com.example.content2.Service;

import com.example.content2.POJO.SoilAnalyse.ExpertSuggestValue;
import com.example.content2.POJO.SoilAnalyse.Result;

import java.util.ArrayList;
import java.util.HashMap;


public interface ExpertSuggestService {

     Double getExpertSuggestValue(Double longitude, Double latitude,
                                        Integer elementId, Integer cropTypeId);

     int insertNewExpertSuggestValue(ExpertSuggestValue expertSuggestValue);

     Result insertNewExpertSuggest(ExpertSuggestValue expertSuggestValue);

     Result insertNewExpertSuggests(Double latitude, Double longitude, String cropName, HashMap<String,Double >nameAndValue );

     boolean isExistSuggestValue(Double longitude, Double latitude, Integer elementId, Integer cropTypeId);

     boolean isExistSuggestValueByBean(ExpertSuggestValue expertSuggestValue);

     int updateSuggestValue(ExpertSuggestValue expertSuggestValue);

     ArrayList<ExpertSuggestValue> getExpertSuggestValueByLimit(Integer page,Integer size);

     int deleteById(Integer id);

     Integer getExpertValueTotal();
}
