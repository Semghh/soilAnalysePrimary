package com.example.content2.Service;


import com.example.content2.POJO.Menu;

import java.util.ArrayList;

public interface MenuService {

    //通过一个顶级菜单id数组，获得一个菜单树
    public Menu[] getMenuGroupByMenuIdGroup(Integer[] MenuIdGroup);

    //根据MenuId 获得一个完整的Menu。包括所有子Menu树
    public Menu getCompleteMenuById(Integer id);

    public int safeInsertNewMenu(Menu menu);

    public boolean isExistMenuById(Integer id);

    public ArrayList<Menu> findAimURLPath(Menu root,String aimURL);

}
