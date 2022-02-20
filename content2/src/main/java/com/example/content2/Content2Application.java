package com.example.content2;

import com.example.content2.POJO.Menu;
import com.example.content2.Service.Impl.MenuServiceImpl;
import com.example.content2.Service.MenuService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;


@SpringBootApplication
public class Content2Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Content2Application.class, args);
    }

}
