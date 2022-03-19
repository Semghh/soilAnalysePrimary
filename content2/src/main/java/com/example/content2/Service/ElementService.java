package com.example.content2.Service;

import com.example.content2.POJO.SoilAnalyse.Element;
import org.apache.ibatis.annotations.Select;

public interface ElementService {
    @Select("select * from element where id=#{id}")
    public Element select(Integer id);

    @Select("select * from element where expression_=#{expression}")
    public Element selectByExpression(String expression);

    public Integer getElementIdByTranslation(String elementName);

    public Integer getElementIdByExpression(String expression);

}
