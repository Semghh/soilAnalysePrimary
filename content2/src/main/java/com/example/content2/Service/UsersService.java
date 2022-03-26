package com.example.content2.Service;

import com.example.content2.POJO.SoilAnalyse.Result;
import com.example.content2.POJO.SoilAnalyse.Users;

import java.util.ArrayList;
import java.util.HashMap;

public interface UsersService {


    int isExistsUsername(String username);

    int registerNewUser(Users u);

    Users selectByUsernameAndPassword(Users u);

    String getRolesById(Integer id);

    String getRolesByUsername(String username);

    ArrayList<Users> getUsersByLimit(Integer page, Integer size);

    Result insertNewUsers(HashMap map);

    Result deleteUserById(HashMap map);

    Result dynamicUpdateUser(HashMap map);

    Result getUsersTotal();

    Result getDistinctRoles();

    boolean checkLoginPass(String password, String username);

    String getHashPWbyUsername(String username);


    Result checkRepeatUsername(String username);
}
