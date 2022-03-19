package com.example.content2.Controller.MenuControllers;

import com.example.content2.POJO.SoilAnalyse.Menu;
import com.example.content2.POJO.SoilAnalyse.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Menu")
public class MenuTreeController {


    private Menu[] defaultMenuTree;
    private Menu[] adminMenuTree;

    @Autowired
    public MenuTreeController(@Qualifier("adminMenuTreeObject") Menu[] adminMenuTree,@Qualifier("defaultMenuGroup") Menu[] defaultMenuTree) {
        this.adminMenuTree = adminMenuTree;
        this.defaultMenuTree = defaultMenuTree;
    }

    @GetMapping("/adminMenuTree")
    public Result getAdminMenuTree(){
        return Result.getInstance(200,"查询成功",adminMenuTree);
    }


    @GetMapping("/getByRoles")
    public Result getByRoles(@RequestParam String roles){
        if (roles.isEmpty()){
            return Result.getInstance(500,"找不到roles",null);
        }
        if (roles.equals("superAdmin")){
            return Result.getInstance(200,"查询成功",defaultMenuTree);
        }else if (roles.equals("admin")){
            return Result.getInstance(200,"查询成功",adminMenuTree);
        }
        return Result.getInstance(200,"roles错误",roles);

    }

}
