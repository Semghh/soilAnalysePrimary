package com.example.content2.Controller;

import com.example.content2.POJO.Result;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
public class TouristController {


    @GetMapping("/ip")
    public Result getRemoteAddress(HttpServletRequest httpRequest){
        String remoteAddr = httpRequest.getRemoteAddr();
        String remoteUser = httpRequest.getRemoteUser();
        String remoteHost = httpRequest.getRemoteHost();
        System.out.println("remoteAddr" + remoteAddr);
        System.out.println("remoteUser" + remoteUser);
        System.out.println("remoteHost" + remoteHost);
        HashMap<String, Object> map = new HashMap<>();
        map.put("remoteAddr",remoteAddr);
        map.put("remoteUser",remoteUser);
        map.put("remoteHost",remoteHost);
        return Result.getInstance(200,"查询成功",map);
    }
}
