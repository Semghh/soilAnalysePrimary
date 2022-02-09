package com.example.content2.Controller.UserControllers;

import com.example.content2.POJO.Result;
import com.example.content2.Service.UsersService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {

    @Resource
    private UsersService usersService;


    @GetMapping("/getByLimit")
    public Result getUsersByLimit(@RequestParam Integer page,@RequestParam Integer size){
        if (page==null || size==null || page<=0 || size<=0){
            return Result.getInstance(500,"参数错误",new Object[]{page,size});
        }
        return Result.getInstance(200,"查询成功",usersService.getUsersByLimit(page,size));
    }

    @PostMapping("/insert")
    public Result insertNewUsers(@RequestBody HashMap map){
        return usersService.insertNewUsers(map);
    }

    @PostMapping("/delete")
    public Result deleteUser(@RequestBody HashMap map){
        return usersService.deleteUserById(map);
    }

    @PostMapping("/update")
    public Result updateUser(@RequestBody HashMap map){
        return usersService.dynamicUpdateUser(map);
    }

    @GetMapping("/getTotal")
    public Result getUsersTotal(){
        return usersService.getUsersTotal();
    }

    @GetMapping("/roles")
    public Result getDistinctRoles(){
        return usersService.getDistinctRoles();
    }
}
