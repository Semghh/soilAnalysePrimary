package com.example.content2.Service;

import com.example.content2.POJO.Element;
import org.apache.ibatis.annotations.Select;

import java.util.Vector;

public interface ElementService {
    @Select("select * from element where id=#{id}")
    public Element select(Integer id);

    @Select("select * from element where expression_=#{expression}")
    public Element selectByExpression(String expression);

    public Integer getElementIdByTranslation(String elementName);

    public Integer getElementIdByExpression(String expression);

}
