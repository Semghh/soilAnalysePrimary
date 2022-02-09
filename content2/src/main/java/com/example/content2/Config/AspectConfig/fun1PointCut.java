package com.example.content2.Config.AspectConfig;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Component
@EnableAspectJAutoProxy
public class fun1PointCut {


    @Pointcut("execution(public * com.example.content2.Service.SuggestValueService.*(..)   )")
    public void fun1PointCut(){

    }
}
