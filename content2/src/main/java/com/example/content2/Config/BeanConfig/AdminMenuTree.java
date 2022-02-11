package com.example.content2.Config.BeanConfig;

import com.example.content2.POJO.Menu;
import com.example.content2.Service.Impl.MenuServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.Resource;
import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@DependsOn({"MenuService"})
public class AdminMenuTree {

    @Resource
    MenuServiceImpl menuService;

    @Bean("adminMenuTreeObject")
    public Menu[] getAdminMenuTree(){
        Integer[]  ids =  new Integer[]{1,4,7};
        return menuService.buildMenuTreeById(ids);
    }

    //默认菜单
    @Bean("defaultMenuGroup")
    public Menu[] geDefaultMenuGroup(){
        Menu build1 = Menu.builder().id(1).type(1).icon("monitor").name("策略管理").url("/strategy").children(new Menu[]{
                        Menu.builder().id(2).type(2).icon(null).name("建议值管理").children(null).url("/strategy/suggest").build(),
                        Menu.builder().id(3).type(2).icon(null).name("专家值管理").children(null).url("/strategy/expert").build()
                }
        ).build();
        Menu build2 = Menu.builder().id(4).type(1).icon("location-information").name("测量点管理").url("/measure").children(new Menu[]{
                        Menu.builder().id(5).type(2).icon(null).name("文件传输").children(null).url("/measure/excel").build(),
                        Menu.builder().id(6).type(2).icon(null).name("测量点管理").children(null).url("/measure/soil").build()
                }
        ).build();
        Menu build3 = Menu.builder().id(7).type(1).icon("crop").name("作物管理").url("/crop").children(new Menu[]{
                        Menu.builder().id(8).type(2).icon(null).name("作物种类管理").children(null).url("/crop/type").build()
                }
        ).build();
        Menu build4 = Menu.builder().id(9).type(1).icon("user").name("用户管理").url("/users").children(new Menu[]{
                        Menu.builder().id(10).type(2).icon(null).name("用户管理").url("/users/user").children(null).build()
                }
        ).build();
        return new Menu[]{build1,build2,build3,build4};
    }

    @Bean("tempExcelTimerQueue")
    public ConcurrentHashMap<File,Long> tempExcelTimerQueue(){
        return new ConcurrentHashMap<>();
    }
}
