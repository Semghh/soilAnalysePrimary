package com.example.content2.Config.TokenConfigs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenConfig {

    @Bean("tokenExpireTime")
    public Long expireTime(){
        Long expireTime = new Long(1000 * 60 * 2 );
        return expireTime;
    }
}
