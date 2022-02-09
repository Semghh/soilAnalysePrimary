package com.example.content2.Aspect;

import com.example.content2.POJO.Result;
import com.example.content2.Util.DoubleFormat;
import com.example.content2.Util.getFieldFromMap;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Aspect
@Component
@DependsOn({"fun1PointCut"})
public class fun1Aspect {


    @Around(value = "com.example.content2.Config.AspectConfig.fun1PointCut.fun1PointCut()")
    public Object afterNotice(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("aop!");
        Object[] args = joinPoint.getArgs();
        Object proceed = joinPoint.proceed(args);
        if (proceed.getClass() == com.example.content2.POJO.Result.class){
            Result p = (Result) proceed;
            Object data = p.getData();

            if (data.getClass() == HashMap.class){

                HashMap map = (HashMap) data;
                String[] fieldNames = new String[]{
                        "sug_Olsen_K","sug_Olsen_P","sug_Effective_N"};
                Class[] fieldClz = new Class[]{Double.class,Double.class,Double.class};
                Object[] field = getFieldFromMap.getField(map, fieldNames, fieldClz);

                Double K2SO4 = (Double)field[0] / 0.6;

                Double KCL = (Double)field[0]/0.5;

                Double erAn = (Double) field[1]/0.46;

                Double niaoSu = ((Double) field[2]-erAn*0.16)/0.6;

                map.put("sug_K2SO4", DoubleFormat.format(2,K2SO4));
                map.put("sug_KCL", DoubleFormat.format(2,KCL));
                map.put("sug_erAn", DoubleFormat.format(2,erAn));
                map.put("sug_niaoSu", DoubleFormat.format(2,niaoSu));
                return proceed;
            }


        }else {

            System.out.println("proceed.class is "+proceed.getClass());
        }
        return proceed;
    }
}
