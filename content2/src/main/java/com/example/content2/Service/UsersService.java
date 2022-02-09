package com.example.content2.Service;

import com.example.content2.POJO.Peasant;
import com.example.content2.POJO.Result;
import com.example.content2.POJO.Users;

import java.util.ArrayList;
import java.util.HashMap;

public interface UsersService {


    public int isExistsUsername(String username);

    public int registerNewUser(Users u);

    public Users selectByUsernameAndPassword(Users u);

    public String getRolesById(Integer id);

    public String getRolesByUsername(String username);

    public ArrayList<Users> getUsersByLimit(Integer page,Integer size);

    public Result insertNewUsers(HashMap map);

    public Result deleteUserById(HashMap map);

    public Result dynamicUpdateUser(HashMap map);

    public Result getUsersTotal();

    public Result getDistinctRoles();

    public boolean checkLoginPass(String password,String username);

    public String getHashPWbyUsername(String username);


    public Result checkRepeatUsername(String username);
}
