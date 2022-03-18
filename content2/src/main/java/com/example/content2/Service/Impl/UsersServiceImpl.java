package com.example.content2.Service.Impl;

import com.example.content2.Mapper.UsersMapper;
import com.example.content2.POJO.Result;
import com.example.content2.POJO.Users;
import com.example.content2.Service.UsersService;
//import com.example.content2.Util.BCrypt;
import com.example.content2.Util.getFieldFromMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service("UsersService")
public class UsersServiceImpl implements UsersService {

    private UsersMapper usersMapper;


    @Autowired
    public UsersServiceImpl(UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
    }

    @Override
    public int isExistsUsername(String username) {
        return usersMapper.isExistsUsername(username);
    }

    @Override
    public int registerNewUser(Users u) {
        return usersMapper.registerNewPeasantUser(u);
    }

    @Override
    public Users selectByUsernameAndPassword(Users u) {
        return usersMapper.selectByUsernameAndPassword(u);
    }

    @Override
    public String getRolesById(Integer id) {
        return usersMapper.getRolesById(id);
    }

    @Override
    public String getRolesByUsername(String username) {
        return usersMapper.getRolesByUsername(username);
    }

    @Override
    public ArrayList<Users> getUsersByLimit(Integer page, Integer size) {
        return usersMapper.getUsersByLimit((page-1)*size,size);
    }

    @Override
    public Result insertNewUsers(HashMap map) {
        String[] strs = {"username", "password", "peasant_id", "roles"};
        Class[] clz = {String.class, String.class, Integer.class, String.class};
        try {
            Object[] field = getFieldFromMap.get(map, strs, clz);
            Users users = new Users(null,(String)field[0],(String)field[1],(Integer) field[2],(String)field[3]);
            int i = usersMapper.insertNewUser(users);
            if (i==1){
                return Result.getInstance(200,"注册成功",null);
            }else {
                return Result.getInstance(501,"未知错误",map);
            }
        } catch (getFieldFromMap.notFoundSuchField notFoundSuchField) {
            notFoundSuchField.printStackTrace();
            return Result.getInstance(500,"未知错误",map);
        }
    }

    @Override
    public Result deleteUserById(HashMap map) {
        try {
            Object[] field = getFieldFromMap.get(map, new String[]{"id"}, new Class[]{Integer.class});
            int i = usersMapper.deleteUserById((Integer) field[0]);
            if (i==1){
                return Result.getInstance(200,"删除成功",field[0]);
            }else {
                return Result.getInstance(500,"未知错误",map);
            }

        } catch (getFieldFromMap.notFoundSuchField e) {
            e.printStackTrace();
            return Result.getInstance(500,e.getMessage(),map);
        }
    }

    @Override
    public Result dynamicUpdateUser(HashMap map) {
        String[] strs = {"id", "password", "peasant_id", "roles"};
        Class[] clz = {Integer.class, String.class, Integer.class, String.class};
        try {
            Object[] field = getFieldFromMap.get(map, strs, clz);
            Users user = new Users();
            user.setId((Integer) field[0]);
            user.setPassword_((String) field[1]);
            user.setPeasant_id((Integer) field[2]);
            user.setRoles((String) field[3]);
            int i = usersMapper.dynamicUpdateUser(user);
            if (i==1){
                return Result.getInstance(200,"修改成功",null);
            }else {
                return Result.getInstance(500,"未知错误",map);
            }
        } catch (getFieldFromMap.notFoundSuchField e) {
            e.printStackTrace();
            return Result.getInstance(500,e.getMessage(),map);
        }
    }

    @Override
    public Result getUsersTotal() {
        try {
            int usersTotal = usersMapper.getUsersTotal();
            return Result.getInstance(200,"查询成功",usersTotal);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getInstance(500,"未知错误",null);
        }
    }

    @Override
    public Result getDistinctRoles() {
        try {
            ArrayList<String> distinctRoles = usersMapper.getDistinctRoles();
            return Result.getInstance(200,"查询成功",distinctRoles);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getInstance(500,"未知错误",null);
        }
    }

    @Override
    public boolean checkLoginPass(String password, String username) {
        String hashPW = getHashPWbyUsername(username);
        return hashPW.equals(password);
//        return (BCrypt.checkpw(password,hashPW));
    }

    @Override
    public String getHashPWbyUsername(String username) {
        return usersMapper.getHashPWbyUsername(username);
    }



    public Result checkRepeatUsername(String username){
        int existsUsername = this.isExistsUsername(username);
        if (existsUsername == 0) {
            return Result.getInstance(200, "恭喜你还未注册", null);
        } else {
            return Result.getInstance(207, "该账号已注册", null);
        }
    }
}
