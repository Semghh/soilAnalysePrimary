package com.example.content2.Service.Impl;


import com.example.content2.Mapper.MenuStoreTableMapper;
import com.example.content2.POJO.Menu;
import com.example.content2.POJO.MenuStoreTable;
import com.example.content2.Service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service("MenuService")
public class MenuServiceImpl implements MenuService {

    private MenuStoreTableMapper menuStoreTableMapper;

    @Autowired
    public MenuServiceImpl(MenuStoreTableMapper menuMapper) {
        this.menuStoreTableMapper = menuMapper;
    }

    @Override
    public Menu[] getMenuGroupByMenuIdGroup(Integer[] menuIdGroup) {
        Menu[] res = new Menu[menuIdGroup.length];
        for (int i = 0; i < menuIdGroup.length; i++) {
            res[i] = getCompleteMenuById(menuIdGroup[i]);
        }
        return res;
    }

    @Override
    public Menu getCompleteMenuById(Integer id) {
        MenuStoreTable menuStore = menuStoreTableMapper.getMenuStoreById(id);
        convertTotMenuHandler convertMenuHandler = new convertTotMenuHandler(menuStoreTableMapper, menuStore);
        return convertMenuHandler.getRes();
    }

    @Override
    @Transactional
    public int safeInsertNewMenu(Menu menu) {
        convertToMenuStoreHandler convertMenuStoreHandler = new convertToMenuStoreHandler(menuStoreTableMapper, menu);
        return convertMenuStoreHandler.getRes();
    }


    @Override
    public boolean isExistMenuById(Integer id) {
        return menuStoreTableMapper.isExistMenuById(id)==1;
    }


    @Override
    public ArrayList<Menu> findAimURLPath(Menu root,String aimURL) {
        findURLHandler findURLHandler = new findURLHandler(aimURL, root);
        return findURLHandler.getPath();
    }

    public Menu[] buildMenuTreeById(Integer[] ids){
        Menu[] res = new Menu[ids.length];
        for (int i = 0; i < ids.length; i++) {
            MenuStoreTable menuStoreById = menuStoreTableMapper.getMenuStoreById(ids[i]);
            res[i] = new convertTotMenuHandler(menuStoreTableMapper,menuStoreById).getRes();
        }
        return res;
    }


    //内部工具类，将Menu存储结构转化为数据结构
    static class convertTotMenuHandler {
        Menu menu ;
        MenuStoreTableMapper mapper;
        MenuStoreTable resStoreTable;

        public convertTotMenuHandler(MenuStoreTableMapper mapper, MenuStoreTable resStoreTable) {
            this.mapper = mapper;
            this.resStoreTable = resStoreTable;
        }
        
        private Menu recursion(Menu cur, MenuStoreTable menuStore){
            cur.setId(menuStore.getId());
            cur.setIcon(menuStore.getIcon());
            cur.setName(menuStore.getName());
            cur.setUrl(menuStore.getUrl());
            cur.setType(menuStore.getType());
            Integer[] childIds = Menu.convertChildStringToIdArray(menuStore.getChildren());
            if (childIds==null)return cur;//如果没有子元素 直接返回

            Menu[] child = new Menu[childIds.length];
            for(int i = 0;i<child.length;i++){
                child[i] = recursion(new Menu(),mapper.getMenuStoreById(childIds[i]));
            }
            return cur;
        }

        public Menu getRes(){
            return recursion(new Menu(),resStoreTable);
        }
    }
    //内部工具类，将Menu数据结构。转化为存储结构
    static class convertToMenuStoreHandler {
        private MenuStoreTableMapper menuStoreTableMapper;
        private Menu menu;
        private int successfulTimes = 0;
        
        public convertToMenuStoreHandler(MenuStoreTableMapper menuStoreTableMapper, Menu menu) {
            this.menuStoreTableMapper = menuStoreTableMapper;
            this.menu = menu;
        }

        //递归插入
        private Integer recursion(Menu m){

            MenuStoreTable menuStoreTable = new MenuStoreTable();
            menuStoreTable.setId(m.getId());
            menuStoreTable.setIcon(m.getIcon());
            menuStoreTable.setUrl(m.getUrl());
            menuStoreTable.setName(m.getName());
            menuStoreTable.setType(m.getType());


            Menu[] children = m.getChildren();
            if (children==null || children.length==0){
                menuStoreTable.setChildren(null);
                if (checkSafe(m.getId())){//数据库中不存在当前节点
                    int i = menuStoreTableMapper.insertNewMenuStore(menuStoreTable); //叶子节点插入数据库
                    if (i==1)successfulTimes++;
                }
                return m.getId();
            }

            StringBuilder childStr = new StringBuilder();
            for (int i = 0; i < children.length; i++) {
                childStr.append(recursion(children[i]));
                childStr.append(",");
            }
            childStr.deleteCharAt(childStr.length()-1);
            menuStoreTable.setChildren(childStr.toString());
            if (checkSafe(m.getId())){ //数据库中不存在
                int i = menuStoreTableMapper.insertNewMenuStore(menuStoreTable); //插入当前节点
                if (i == 1) successfulTimes++;
            }
            return m.getId();
        }

        public int getRes(){
            recursion(menu);
            return successfulTimes;
        }

        private boolean checkSafe(Integer id){
            if (id==null)return true;
            if (menuStoreTableMapper.isExistMenuById(id)==1) {
                return false;
            }
            return true;
        }
    }


    //找到指定url,并输出路径. 找不到,返回Null
    static class findURLHandler{
        private String aimURL;
        private Menu root;
        ArrayList<Menu> res;

        public findURLHandler(String aimURL, Menu root) {
            this.aimURL = aimURL;
            this.root = root;
        }

        public ArrayList<Menu> getPath(){
            res = new ArrayList<>();
            if (!recursion(root,res,aimURL)) {
                return null;
            }
            return res;
        }

        private boolean recursion(Menu root, ArrayList<Menu> path,String aimURL){
            path.add(root);
            if (root.getUrl().equals(aimURL)){
                return true;
            }
            Menu[] children = root.getChildren();
            if(children!=null){
                for (Menu child : children) {
                    if (!recursion(child,path,aimURL)) {
                        path.remove(path.size()-1);
                    }else {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
