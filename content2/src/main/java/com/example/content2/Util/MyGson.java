package com.example.content2.Util;


import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;

public class MyGson {
    @Bean
    public Gson inject(){
        return new Gson();
    }
}
