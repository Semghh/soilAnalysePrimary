package com.example.content2.Filter;


import com.example.content2.POJO.SoilAnalyse.Result;
import com.example.content2.Util.TokenStore;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenAuth implements HandlerInterceptor {

    private ConcurrentHashMap<String, TokenStore> tokenMapping;

    @Autowired
    public TokenAuth(@Qualifier("tokenMapping") ConcurrentHashMap<String, TokenStore> tokenMapping) {
        this.tokenMapping = tokenMapping;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (token==null){
            response.setContentType("text/html;charset=utf-8");
            PrintWriter writer = response.getWriter();
            Gson gson = new Gson();
            String str= gson.toJson(Result.getInstance(555, "未收到token请登录", null));
            writer.write(str);
            return false;
        }
        boolean b = tokenMapping.containsKey(token);
        if (!b){
            response.setContentType("text/html;charset=utf-8");
            PrintWriter writer = response.getWriter();
            Gson gson = new Gson();
            String str= gson.toJson(Result.getInstance(556, "token验证失败", null));
            writer.write(str);
            return false;
        }
        return b;
    }
}
