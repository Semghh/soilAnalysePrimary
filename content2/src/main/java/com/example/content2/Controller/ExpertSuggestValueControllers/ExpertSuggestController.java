package com.example.content2.Controller.ExpertSuggestValueControllers;

import com.example.content2.POJO.ExpertSuggestValue;
import com.example.content2.POJO.Result;
import com.example.content2.Service.CropTypesService;
import com.example.content2.Service.ElementService;
import com.example.content2.Service.ExpertSuggestService;
import com.example.content2.Service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.content2.Util.*;


import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class ExpertSuggestController {

    private ExpertSuggestService expertSuggestService;
    private ElementService elementService;
    private CropTypesService cropTypesService;
    private RegionService regionService;

    @Autowired
    public ExpertSuggestController(ExpertSuggestService expertSuggestService, ElementService elementService, CropTypesService cropTypesService, RegionService regionService) {
        this.expertSuggestService = expertSuggestService;
        this.elementService = elementService;
        this.cropTypesService = cropTypesService;
        this.regionService = regionService;
    }


    //插入新的专家建议值
    @PostMapping("/newExpertSuggest")
    @ResponseBody
    public Result insertNewExpertSuggest(@RequestBody HashMap map) {
        Double longitude = Double.parseDouble((String) map.get("longitude"));
        Double latitude = Double.parseDouble((String) map.get("latitude"));
        String cropName = (String) map.get("cropName");
        String elementName = (String) map.get("elementName");
        Double suggestValue = Double.parseDouble((String) map.get("suggestValue"));
        HashMap<Object, Object> returnMap = new HashMap<>();


        Integer elementId = elementService.getElementIdByExpression(elementName);
        if (elementId == null) {
            returnMap.put("elementName", elementName);
            return Result.getInstance(501, "元素表达式不正确", returnMap);
        }

        Integer cropTypeId = cropTypesService.getTypeIdByName(cropName);
        if (cropTypeId == null) {
            returnMap.put("cropName", cropName);
            return Result.getInstance(502, "作物名称不正确", returnMap);
        }
        ExpertSuggestValue expertSuggestValue = new ExpertSuggestValue(null,longitude, latitude, elementId
                , cropTypeId, suggestValue);
        return expertSuggestService.insertNewExpertSuggest(expertSuggestValue);
    }

    //增
    @PostMapping("/newExpertSuggests")
    public Result insertNewExpertSuggests(@RequestBody HashMap map) {

        Double longitude =null;
        Double latitude = null;
        String cropName = null;
        if (!judge.isNullString((String) map.get("longitude"))) {
            longitude = Double.parseDouble((String) map.get("longitude"));
        }else return Result.getInstance(501,"参数longitude丢失",null);
        if (!judge.isNullString((String) map.get("latitude"))){
            latitude = Double.parseDouble((String) map.get("latitude"));
        }else return Result.getInstance(501,"参数latitude丢失",null);
        if (!judge.isNullString((String) map.get("cropName"))){
            cropName = (String) map.get("cropName");
        }else return Result.getInstance(501,"参数cropName丢失",null);
        if (!regionService.isExistRegionByLatLong(longitude,latitude)) {
            HashMap<Object, Object> temp = new HashMap<>();
            temp.put("laitude",latitude);
            temp.put("longitude",longitude);
            return Result.getInstance(501,"不存在的测量点",temp);
        }


        HashMap<String, Double> elementMap = processElement(map);
        return expertSuggestService.insertNewExpertSuggests(latitude,longitude,cropName,elementMap);
    }

    private HashMap<String, Double> processElement(HashMap map) {
        HashMap<String, Double> res = new HashMap<>();
        Double cur;
        if (!judge.isNullString((String) map.get("ph"))) {
            cur = Double.parseDouble((String) map.get("ph"));
            res.put("ph", cur);
        }
        if (!judge.isNullString((String) map.get("organic_matter"))) {
            cur = Double.parseDouble((String) map.get("organic_matter"));
            res.put("organic_matter", cur);
        }
        if (!judge.isNullString((String) map.get("total_nitrogen"))) {
            cur = Double.parseDouble((String) map.get("total_nitrogen"));
            res.put("total_nitrogen", cur);
        }
        if (!judge.isNullString((String) map.get("Olsen_P"))) {
            cur = Double.parseDouble((String) map.get("Olsen_P"));
            res.put("Olsen_P", cur);
        }
        if (!judge.isNullString((String) map.get("Olsen_K"))) {
            cur = Double.parseDouble((String) map.get("Olsen_K"));
            res.put("Olsen_K", cur);
        }
        if (!judge.isNullString((String) map.get("slowly_K"))) {
            cur = Double.parseDouble((String) map.get("slowly_K"));
            res.put("slowly_K", cur);
        }
        if (!judge.isNullString((String) map.get("Olsen_K"))) {
            cur = Double.parseDouble((String) map.get("Olsen_K"));
            res.put("Olsen_K", cur);
        }
        if (!judge.isNullString((String) map.get("Effective_Cu"))) {
            cur = Double.parseDouble((String) map.get("Effective_Cu"));
            res.put("Effective_Cu", cur);
        }
        if (!judge.isNullString((String) map.get("Effective_Zn"))) {
            cur = Double.parseDouble((String) map.get("Effective_Zn"));
            res.put("Effective_Zn", cur);
        }
        if (!judge.isNullString((String) map.get("Effective_Fe"))) {
            cur = Double.parseDouble((String) map.get("Effective_Fe"));
            res.put("Effective_Fe", cur);
        }
        if (!judge.isNullString((String) map.get("Effective_Mn"))) {
            cur = Double.parseDouble((String) map.get("Effective_Mn"));
            res.put("Effective_Mn", cur);
        }
        if (!judge.isNullString((String) map.get("Effective_N"))) {
            cur = Double.parseDouble((String) map.get("Effective_N"));
            res.put("Effective_N", cur);
        }
        return res;
    }

    //查
    @GetMapping("/getExpertSuggests")
    public Result getByLimit(@RequestParam("page") Integer page,@RequestParam("size") Integer size){
        if (page==null || size==null || page<=0 || size<=0){
            return Result.getInstance(500,"参数错误",new Integer[]{page,size});
        }
        try {
            ArrayList<ExpertSuggestValue> data = expertSuggestService.getExpertSuggestValueByLimit(page, size);
            return Result.getInstance(200,"查询成功",data);
        }catch (Exception e){
            return Result.getInstance(501,"未知错误",null);
        }
    }

    @PostMapping("/deleteExpertSuggests")
    public Result deleteById(@RequestBody HashMap map){
        try{
            Integer id = (Integer) getFieldFromMap.get(map,new String[]{"id"},new Class[]{Integer.class})[0];
            int i = expertSuggestService.deleteById(id);
            if (i==1){
                return Result.getInstance(200,"删除成功",id);
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.getInstance(500,"未知错误",map);
        }
        return Result.getInstance(501,"删除失败",map);
    }

    @GetMapping("/getExpertSuggestsTotal")
    public Result getESTotal(){
        try{
            Integer total = expertSuggestService.getExpertValueTotal();
            return Result.getInstance(200,"查询成功",total);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getInstance(500,"未知错误",null);
        }
    }
}
