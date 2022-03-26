package com.example.content2.Service;

import com.example.content2.POJO.SoilAnalyse.Element;
import org.apache.ibatis.annotations.Select;

public interface ElementService {
    @Select("select * from element where id=#{id}")
     Element select(Integer id);

    @Select("select * from element where expression_=#{expression}")
     Element selectByExpression(String expression);

     Integer getElementIdByTranslation(String elementName);

     Integer getElementIdByExpression(String expression);

}
