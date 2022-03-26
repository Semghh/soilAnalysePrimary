package com.example.content2.Service.Impl;

import com.example.content2.Mapper.Primary.CropTypesMapper;
import com.example.content2.Mapper.Primary.ElementMapper;
import com.example.content2.Mapper.Primary.ExpertSuggestValueMapper;
import com.example.content2.Mapper.Primary.RegionMapper;
import com.example.content2.POJO.SoilAnalyse.ExpertSuggestValue;
import com.example.content2.POJO.SoilAnalyse.Result;
import com.example.content2.Service.ExpertSuggestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@Service("ExpertSuggestService")
public class ExpertSuggestServiceImpl implements ExpertSuggestService {

    private final ExpertSuggestValueMapper expertSuggestValueMapper;
    private final ElementMapper elementMapper;
    private final CropTypesMapper cropTypesMapper;
    private final RegionMapper regionMapper;

    @Autowired
    public ExpertSuggestServiceImpl(ExpertSuggestValueMapper expertSuggestValueMapper, ElementMapper elementMapper, CropTypesMapper cropTypesMapper, RegionMapper regionMapper) {
        this.expertSuggestValueMapper = expertSuggestValueMapper;
        this.elementMapper = elementMapper;
        this.cropTypesMapper = cropTypesMapper;
        this.regionMapper = regionMapper;
    }

    @Override
    public Double getExpertSuggestValue(Double longitude, Double latitude, Integer elementId, Integer cropTypeId) {
        return expertSuggestValueMapper.getExpertSuggestValue(longitude,latitude,elementId,cropTypeId);
    }

    @Override
    public int insertNewExpertSuggestValue(ExpertSuggestValue expertSuggestValue) {
        return expertSuggestValueMapper.insertNewExpertSuggestValue(expertSuggestValue);
    }

    @Override
    public Result insertNewExpertSuggest(ExpertSuggestValue expertSuggestValue) {
        try{
            int i = regionMapper.existsRegion(expertSuggestValue.getLongitude(), expertSuggestValue.getLatitude());
            if (i==0){
                HashMap<String,Double> map = new HashMap<>();
                map.put("longitude",expertSuggestValue.getLongitude());
                map.put("latitude",expertSuggestValue.getLatitude());
                return Result.getInstance(400,"不存在的测量点",map);
            }
            if (isExistSuggestValueByBean(expertSuggestValue)){
                if(expertSuggestValueMapper.updateSuggestValueByBean(expertSuggestValue)==1)
                    return Result.getInstance(200,"专家调整成功!",null);
            }else {
                int res = expertSuggestValueMapper.insertNewExpertSuggestValue(expertSuggestValue);
                if (res==1) {
                    return Result.getInstance(200,"专家调整成功!",null);
                }
            }
            return Result.getInstance(500,"添加失败,未知错误,请联系管理员",null);
        }catch (Exception e){
            return Result.getInstance(501,"添加失败,未知错误,请联系管理员",null);
        }
    }

    @Override
    public Result insertNewExpertSuggests(Double latitude, Double longitude, String cropName, HashMap<String, Double> nameAndValue) {
        Integer croptypeId = cropTypesMapper.getTypeIdByName(cropName);
        HashMap<Object, Object> returnMap = new HashMap<>();
        if (croptypeId==null){
            returnMap.put("cropName",cropName);
            return Result.getInstance(502,"作物名称错误",returnMap);
        }
        Set<String> nameSet = nameAndValue.keySet();

        for (String s : nameSet) {
            Integer elementId = elementMapper.getElementIdByExpression(s);
            Double curValue = nameAndValue.get(s);
            ExpertSuggestValue expertSuggestValue = new ExpertSuggestValue(null,longitude,latitude,elementId,croptypeId,curValue);
            if (isExistSuggestValueByBean(expertSuggestValue)){
                if(expertSuggestValueMapper.updateSuggestValueByBean(expertSuggestValue)==1)
                    returnMap.put(s,curValue);
            }
            if (expertSuggestValueMapper.insertNewExpertSuggestValue(expertSuggestValue)==1)
                returnMap.put(s,curValue);
        }
        return Result.getInstance(400,"批量添加专家值成功!",returnMap);
    }

    @Override
    public boolean isExistSuggestValue(Double longitude, Double latitude, Integer elementId, Integer cropTypeId) {
        return expertSuggestValueMapper.existSuggestValue(longitude,latitude,elementId,cropTypeId)==1;
    }

    @Override
    public boolean isExistSuggestValueByBean(ExpertSuggestValue expertSuggestValue) {
        return expertSuggestValueMapper.existSuggestValueByBean(expertSuggestValue)==1;
    }

    @Override
    public int updateSuggestValue(ExpertSuggestValue expertSuggestValue) {
        return expertSuggestValueMapper.updateSuggestValueByBean(expertSuggestValue);
    }

    @Override
    public ArrayList<ExpertSuggestValue> getExpertSuggestValueByLimit(Integer page, Integer size) {
        return  expertSuggestValueMapper.getExpertSuggestValueByLimit((page-1)*size,size);

    }

    @Override
    public int deleteById(Integer id) {
        return expertSuggestValueMapper.deleteById(id);
    }

    @Override
    public Integer getExpertValueTotal() {
        return expertSuggestValueMapper.getTotal();
    }


}
