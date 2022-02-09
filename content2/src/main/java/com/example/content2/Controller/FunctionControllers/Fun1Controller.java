package com.example.content2.Controller.FunctionControllers;


import com.example.content2.POJO.Result;
import com.example.content2.Service.CropTypesService;
import com.example.content2.Service.SuggestValueService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

@RestController
public class Fun1Controller {
    private Log log = LogFactory.getLog(this.getClass());

    private SuggestValueService suggestValueService;
    private CropTypesService cropTypesService;

    @Autowired
    public Fun1Controller( CropTypesService cropTypesService, SuggestValueService suggestValueService) {
        this.suggestValueService = suggestValueService;
        this.cropTypesService = cropTypesService;
    }

    @PostMapping("/fun1")
    public Result postFun1(@RequestBody HashMap bodyParams) {
            String longitudeText = (String) bodyParams.get("longitude");
            String latitudeText = (String) bodyParams.get("latitude");
            String crop_name = (String) bodyParams.get("cropName");
            return suggestValueService.fun1(longitudeText, latitudeText, crop_name, bodyParams);
    }
    @GetMapping("/fun1")
    public Result getFun1(@RequestParam HashMap urlParams) {
        synchronized (this){
            String longitudeText = (String) urlParams.get("longitude");
            String latitudeText = (String) urlParams.get("latitude");
            String crop_name = (String) urlParams.get("cropName");
            return suggestValueService.fun1(longitudeText, latitudeText, crop_name, urlParams);
        }
    }

    @GetMapping("/getEnableCropTypeName")
    public Result getAllEnableCropTypeName(){
        Vector<String> allEnableCropName = cropTypesService.getAllEnableCropName();
        return Result.getInstance(200,"获取成功!",allEnableCropName.toArray());
    }


}
