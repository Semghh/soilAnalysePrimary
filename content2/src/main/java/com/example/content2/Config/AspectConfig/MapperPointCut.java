package com.example.content2.Config.AspectConfig;

import com.example.content2.Annotation.QueryName;
import com.example.content2.POJO.SoilAnalyse.QueryKeepTime;
import com.example.content2.Service.QueryKeepTimeService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Aspect
@Component
public class MapperPointCut {

    @Resource
    private QueryKeepTimeService queryKeepTimeService;

    @Pointcut(value = "execution(public * com.example.content2.Mapper.*.*(..)) &&" +
            "!execution(public * com.example.content2.Mapper.Primary.QueryKeepTimeMapper.*(..)) && "+
            "@annotation(com.example.content2.Annotation.MonitorQuery)")
    private void pointCut(){};

    private final ExecutorService single = Executors.newFixedThreadPool(1);

    @Around(value = "pointCut()")
    public Object KeepQueryTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();//获取接入点
        Signature signature = joinPoint.getSignature();//获取签名

        if(signature instanceof MethodSignature){ //如果是方法签名
            long start = System.currentTimeMillis();//记录开始时间
            MethodSignature ms = (MethodSignature) signature;//强转
            Method method = ms.getMethod();//获得方法本体
            String queryName = "";
            if (method.isAnnotationPresent(QueryName.class)) {//如果标注了QueryName 注解
                QueryName annotation = method.getAnnotation(QueryName.class);
                queryName = annotation.value();
            }
            Object proceed = joinPoint.proceed(args);
            long end = System.currentTimeMillis();
            asyncInsert(start,end,signature.toString()
                    ,queryName, Arrays.toString(args));//异步执行数据库插入操作,在多核心下,减少对主功能的响应影响。
            return proceed;
        }
        return joinPoint.proceed(args);
    }

    private void asyncInsert(long start,long end,String signature,String queryName,String params){

        single.submit(()->{
            queryKeepTimeService.createNewRecord(
                    new QueryKeepTime(null,start,(end-start),signature,queryName,params));
        });

    }

}
