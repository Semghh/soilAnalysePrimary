package com.example.content2.Mapper;

import com.example.content2.POJO.Users;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface UsersMapper {

    @Select("select count(*) from users where username_=#{username}")
    public int isExistsUsername(String username);

    @Select("select * from users where username_=#{username_} and password_=#{password_}")
    public Users selectByUsernameAndPassword(Users u);


    public int registerNewPeasantUser(Users u);

    @Select("select `roles` from `users` where `id`=#{id} ")
    public String getRolesById(Integer id);

    @Select("select `roles` from `users` where `username_`=#{username} ")
    public String getRolesByUsername(String username);

    public ArrayList<Users> getUsersByLimit(Integer index,Integer size);

    public int insertNewUser(Users u);

    public int deleteUserById(Integer id);

    public int dynamicUpdateUser(Users u);

    public int getUsersTotal();

    public ArrayList<String> getDistinctRoles();

    public String getHashPWbyUsername(String username);
}
