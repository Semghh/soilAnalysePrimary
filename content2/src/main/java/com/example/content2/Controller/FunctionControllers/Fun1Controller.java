package com.example.content2.Controller.FunctionControllers;


import com.example.content2.POJO.SoilAnalyse.Result;
import com.example.content2.Service.CropTypesService;
import com.example.content2.Service.SuggestValueService;

import com.example.content2.Util.StringUtil;
import com.example.content2.Util.getFieldFromMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Vector;

@RestController
public class Fun1Controller {
    private Log log = LogFactory.getLog(this.getClass());

    private SuggestValueService suggestValueService;
    private CropTypesService cropTypesService;

    @Autowired
    public Fun1Controller(CropTypesService cropTypesService, SuggestValueService suggestValueService) {
        this.suggestValueService = suggestValueService;
        this.cropTypesService = cropTypesService;
    }

    @RequestMapping("/fun1")
    public Result postFun1(@RequestParam HashMap bodyParams, HttpSession session, HttpServletRequest request) throws getFieldFromMap.notFoundSuchField {

        String[] paramNames = new String[]{"longitude","latitude","cropName"};
        Class[] clz = new Class[]{String.class,String.class,String.class};
        Object[] o = getFieldFromMap.get(bodyParams, paramNames, clz);
        String loginResult = (String) session.getAttribute("loginResult");
        if (loginResult==null || !loginResult.equals("true"))
            return Result.getInstance(409,"请先登录",null);

        String remoteAddr = request.getRemoteAddr();
        boolean isTourist = false;
        if (StringUtil.NotAllAndEquals((String) session.getAttribute("roles"),"tourist"))isTourist = true;
        return suggestValueService.fun1((String)o[0], (String)o[1], (String)o[2],remoteAddr,isTourist);
    }

    @GetMapping("/getEnableCropTypeName")
    public Result getAllEnableCropTypeName() {
        Vector<String> allEnableCropName = cropTypesService.getAllEnableCropName();
        return Result.getInstance(200, "获取成功!", allEnableCropName.toArray());
    }


}
