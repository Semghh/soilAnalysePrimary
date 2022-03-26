package com.example.content2.Controller.Secondary;

import com.example.content2.POJO.SoilAnalyse.Result;
import com.example.content2.Util.*;
import com.example.content2.Service.fun1Calculate.CalculateRecordService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

@RestController
@RequestMapping("/calculate")
public class CalculateController {


    @Resource
    private CalculateRecordService calculateRecordService;

    @RequestMapping("/do")
    public Result doCalculate(@RequestBody HashMap<String,Object> map) {
        return calculateRecordService.calculateTemplate((Integer) map.get("loopTimes"),
                (Double)map.get("offset"),(Double)map.get("offsetIncrement"),
                (Integer) map.get("fori"),(Integer) map.get("magnification"));
    }
}
