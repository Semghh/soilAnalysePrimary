package com.example.content2.Mapper;


import com.example.content2.Annotation.MonitorQuery;
import com.example.content2.Annotation.QueryName;
import com.example.content2.POJO.MenuStoreTable;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuStoreTableMapper {

    @MonitorQuery
    @QueryName("通过MenuId 查询一个MenuStore")
    public MenuStoreTable getMenuStoreById(Integer id);

    public int insertNewMenuStore(MenuStoreTable menuStoreTable);

    public int isExistMenuById(Integer id);

}
