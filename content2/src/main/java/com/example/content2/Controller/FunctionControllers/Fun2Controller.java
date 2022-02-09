package com.example.content2.Controller.FunctionControllers;

import com.example.content2.POJO.Result;
import com.example.content2.Service.CropTypesService;
import com.example.content2.Service.SuggestValueService;
import com.example.content2.Util.FormatAndOut;
import com.example.content2.Util.PostGetParams;
import com.example.content2.Util.StringLengthGreaterThanZero;
import com.example.content2.Util.judgeOnlyNumber;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Vector;

@RestController
public class Fun2Controller {
    private Double[] sugValues = new Double[4];
    private SuggestValueService suggestValueDAOMapper;
    private CropTypesService cropTypesDAOMapper;
    private Log log  = LogFactory.getLog(this.getClass());

    @Autowired
    public Fun2Controller(SuggestValueService suggestValueDAOMapper, CropTypesService cropTypesDAOMapper) {
        this.suggestValueDAOMapper = suggestValueDAOMapper;
        this.cropTypesDAOMapper = cropTypesDAOMapper;
    }

    @PostMapping("/fun2")
    public Result fun2(@RequestBody HashMap bodyParams){
        //获取请求参数
        String mea_Effective_N = (String) bodyParams.get("mea_Effective_N");
        String mea_Olsen_P = (String) bodyParams.get("mea_Olsen_P");
        String mea_Olsen_K = (String) bodyParams.get("mea_Olsen_K");
        String mea_Organic_matter = (String) bodyParams.get("mea_Organic_matter");
        String typeName = (String) bodyParams.get("cropName");
        return realFun2(mea_Effective_N,mea_Olsen_P,mea_Olsen_K,mea_Organic_matter,typeName,bodyParams);
    }
    @GetMapping("/fun2")
    public Result fun2_(@RequestParam HashMap urlParams){
        //获取请求参数
        String mea_Effective_N = (String) urlParams.get("mea_Effective_N");
        String mea_Olsen_P = (String) urlParams.get("mea_Olsen_P");
        String mea_Olsen_K = (String) urlParams.get("mea_Olsen_K");
        String mea_Organic_matter = (String) urlParams.get("mea_Organic_matter");
        String typeName = (String) urlParams.get("cropName");
        return realFun2(mea_Effective_N,mea_Olsen_P,mea_Olsen_K,mea_Organic_matter,typeName,urlParams);
    }




    public Result realFun2(String mea_Effective_N,String mea_Olsen_P,String mea_Olsen_K,String mea_Organic_matter,String typeName
                            ,HashMap params){
        //data  <k,v>  <==> <元素值名,值>
        HashMap<String, String> Map = new HashMap<>();


        //检查参数合法性
        Result result =null;

        if ((result=passCheckParamsLegality(mea_Effective_N, mea_Olsen_P, mea_Olsen_K, mea_Organic_matter, typeName, params))!=null){
            return result;
        }


        //获得农作物的typeId
        Integer typeId = cropTypesDAOMapper.getTypeIdByName(typeName);
        //发送json时传递给前端的字段名
        Vector<String> names = new Vector<>();
        names.add("sug_Effective_N");
        names.add("sug_Olsen_P");
        names.add("sug_Olsen_K");
        names.add("sug_Organic_matter");
        Vector<Integer> flags = new Vector<>();
        flags.clear();

        //查询sql数据库，对应元素的字段名
        Vector<String> meaNameField = new Vector<>();
        meaNameField.add("Effective_N");
        meaNameField.add("Olsen_P");
        meaNameField.add("Olsen_K");
        meaNameField.add("Organic_matter");

        //从请求参数中获得的各项元素测量值
        Vector<String> meaName = new Vector<>();
        meaName.add(mea_Effective_N);
        meaName.add(mea_Olsen_P);
        meaName.add(mea_Olsen_K);
        meaName.add(mea_Organic_matter);

        //查询各个元素的建议值
        getSugValue(flags,meaName,typeId,meaNameField,suggestValueDAOMapper);


        FormatAndOut formatAndOut = new FormatAndOut();
        formatAndOut.formatAndOut(Map, names, flags,sugValues);


        log.info("fun2 查询值 : "+Map);
        return Result.getInstance(200,"查询成功", Map);
    }
    //查询各个元素的建议值
    //
    //
    //耦合度较高、 需要 names meaNameField meaName 里面元素顺序相对应。
    //修改查询元素的时候。需要同时变动上面各个Vector的值，成员变量 sugValues 的长度也需要变
    //原因是,查询时需要sql的各个元素字段，且有查询顺序，查询出结果后，发json又需要封装字段
    private void getSugValue(Vector<Integer> flags, Vector<String> meaName, Integer typeId, Vector<String> meaNameField, SuggestValueService suggestValueDAOMapper){
        for(int i = 0;i<4;i++){
            if (StringLengthGreaterThanZero.judge(meaName.get(i)))  {
                if (judgeOnlyNumber.judgeOnlyNumber(meaName.get(i))) {
                    sugValues[i] = suggestValueDAOMapper.selectResult(typeId, meaNameField.get(i), Double.valueOf(meaName.get(i)));
                    if(sugValues[i] != null) {
                        flags.add(1);
                    } else {
                        flags.add(0);
                    }
                }
            } else {
                flags.add(0);
            }
        }

    }

    private Result passCheckParamsLegality(String mea_Effective_N,String mea_Olsen_P,String mea_Olsen_K,String mea_Organic_matter,String typeName,HashMap params){
        if (mea_Effective_N!=null && !judgeOnlyNumber.judgeOnlyNumber(mea_Effective_N))
            return Result.getInstance(420,"碱解氮(mea_Effective_N)参数错误",params);
        if (mea_Olsen_P!=null && !judgeOnlyNumber.judgeOnlyNumber(mea_Olsen_P))
            return Result.getInstance(421,"有效磷(mea_Olsen_P)参数错误",params);
        if (mea_Olsen_K!=null && !judgeOnlyNumber.judgeOnlyNumber(mea_Olsen_K))
            return Result.getInstance(422,"速效钾(mea_Olsen_K)参数错误",params);
        if (mea_Organic_matter!=null && !judgeOnlyNumber.judgeOnlyNumber(mea_Organic_matter))
            return Result.getInstance(423,"有机质(mea_Organic_matter)参数错误",params);
        if (!StringLengthGreaterThanZero.judge(typeName))
            return Result.getInstance(424,"作物名称(cropName)参数错误",params);
        return null;
    }
}
