package com.example.content2.Config.AspectConfig;

import com.example.content2.POJO.Fun1Cost;
import com.example.content2.Service.Fun1CostService;
import com.example.content2.Util.RedisMsgQueue;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Aspect
public class Fun1PointCut {
    @Resource(name = "defaultRedisTemplate")
    private RedisTemplate redisTemplate;

    @Resource
    private Fun1CostService fun1CostService;

    @Pointcut("execution(public * com.example.content2.Service.SuggestValueService.fun1(..)   )")
    public void fun1PointCut(){
    }

    @Around(value = "fun1PointCut()")
    public Object fun1Count(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed(joinPoint.getArgs());
        long end = System.currentTimeMillis();
        fun1CostService.newFun1Cost(new Fun1Cost(null,start,end-start));
        RedisMsgQueue.submit(()->redisTemplate.opsForValue().increment("fun1Count"));
        return proceed;
    }



}
