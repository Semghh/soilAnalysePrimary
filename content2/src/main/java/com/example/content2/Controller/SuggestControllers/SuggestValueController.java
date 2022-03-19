package com.example.content2.Controller.SuggestControllers;

import com.example.content2.POJO.SoilAnalyse.Result;
import com.example.content2.Service.SuggestValueService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/suggestValues")
public class SuggestValueController {

    private Log log = LogFactory.getLog(this.getClass());

    @Resource
    private SuggestValueService suggestValueService;


    //获取建议值
    @GetMapping("/getByLimit")
    public Result getSuggestValueByLimit(@RequestParam("page") Integer page,@RequestParam("size") Integer size){
        if (page==null || size==null || page<=0 || size<=0){
            return Result.getInstance(201,"参数错误",null);
        }
        try {
            ArrayList<HashMap<String, Object>> data = suggestValueService.getSuggestValuesByLimit(page, size);
            return Result.getInstance(200,"获取成功",data);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getInstance(500,"未知错误",null);
        }
    }

    @PostMapping("/update")
    public Result updateSuggestValue(@RequestBody HashMap<String,Object> map){
        return suggestValueService.updateSuggestValue(map);
    }
    @PostMapping("/insert")
    public Result insertSuggestValue(@RequestBody HashMap<String,Object> map){
        return suggestValueService.insertNewSuggestValue(map);
    }

    @PostMapping("/delete")
    public Result deleteSuggestValue(@RequestBody HashMap map){
        return suggestValueService.deleteSuggestValue(map);
    }

    @GetMapping("/total")
    public Result getTotal(){
        Integer total = suggestValueService.getSuggestValueTotal();
        return Result.getInstance(200,"查询成功",total);
    }

    @PostMapping("/excel")
    public Result getExcel(@RequestBody HashMap<String,Object> map){
        return suggestValueService.getExcelURl(map);
    }

}
