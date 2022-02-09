package com.example.content2.Config.TokenConfigs;

import com.example.content2.Filter.TokenAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;

//@Configuration
public class TokenFilterConfig implements WebMvcConfigurer {

    private TokenAuth tokenAuthInterceptor;

    @Autowired
    public TokenFilterConfig(TokenAuth tokenAuthInterceptor) {
        this.tokenAuthInterceptor = tokenAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(tokenAuthInterceptor);
        registration.addPathPatterns("/**")
                .excludePathPatterns("/login","/login/**");
    }

}
