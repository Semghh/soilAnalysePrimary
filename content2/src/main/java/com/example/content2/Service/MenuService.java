package com.example.content2.Service;


import com.example.content2.POJO.SoilAnalyse.Menu;

import java.util.ArrayList;

public interface MenuService {

    //通过一个顶级菜单id数组，获得一个菜单树
     Menu[] getMenuGroupByMenuIdGroup(Integer[] MenuIdGroup);

    //根据MenuId 获得一个完整的Menu。包括所有子Menu树
     Menu getCompleteMenuById(Integer id);

     int safeInsertNewMenu(Menu menu);

     boolean isExistMenuById(Integer id);

     ArrayList<Menu> findAimURLPath(Menu root,String aimURL);

}
