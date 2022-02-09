package com.example.content2.Config.BeanConfig;

import com.example.content2.POJO.Menu;
import com.example.content2.Util.TokenStore;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class MyBeans {


    @Bean("tokenMapping")
    public ConcurrentHashMap<String, TokenStore> getTokenMapping(){
        return new ConcurrentHashMap<String, TokenStore>();
    }
}
