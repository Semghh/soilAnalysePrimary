package com.example.content2.Controller.MenuControllers;

import com.example.content2.POJO.Menu;
import com.example.content2.POJO.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@DependsOn("defaultMenuGroup")
public class MenuController {

    private Menu[] defaultMenuGroup;

    @Autowired
    public MenuController(@Qualifier("defaultMenuGroup") Menu[] defaultMenuGroup) {
        this.defaultMenuGroup = defaultMenuGroup;
    }

    @RequestMapping("/getDefaultMenuGroup")
    @ResponseBody
    public Result getDefaultMenuGroup(){
        return Result.getInstance(200,"默认菜单树",defaultMenuGroup);
    }

}
