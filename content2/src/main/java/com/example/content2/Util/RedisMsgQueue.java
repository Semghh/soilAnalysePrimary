package com.example.content2.Util;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.IntStream;

@Component
public class RedisMsgQueue {

    static int parallelSize = Runtime.getRuntime().availableProcessors() + 1;

    private static ExecutorService executorService = Executors.newFixedThreadPool(parallelSize);

    public static Future submit(Runnable r){
        return executorService.submit(r);
    }

    public static <T,R> R doSomething(Function<T,R> fun,T t){

        return fun.apply(t);
    }
}
