package com.example.content2.Service.Impl;

import com.example.content2.Mapper.ElementMapper;
import com.example.content2.POJO.Element;
import com.example.content2.Service.ElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Vector;

@Service("ElementService")
public class ElementServiceImpl implements ElementService {

    private ElementMapper elementMapper;


    @Autowired
    public ElementServiceImpl(ElementMapper elementMapper) {
        this.elementMapper = elementMapper;
    }

    @Override
    public Element select(Integer id) {
        return elementMapper.selectById(id);
    }

    @Override
    public Element selectByExpression(String expression) {
        return elementMapper.selectByExpression(expression);
    }

    @Override
    public Integer getElementIdByTranslation(String elementName) {
        return elementMapper.getElementIdByTranslation(elementName);
    }

    @Override
    public Integer getElementIdByExpression(String expression) {
        return elementMapper.getElementIdByExpression(expression);
    }


}
