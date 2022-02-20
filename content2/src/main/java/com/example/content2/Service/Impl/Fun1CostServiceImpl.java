package com.example.content2.Service.Impl;

import com.example.content2.Mapper.Fun1CostMapper;
import com.example.content2.POJO.Fun1Cost;
import com.example.content2.Service.Fun1CostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("Fun1CostService")
public class Fun1CostServiceImpl implements Fun1CostService {

    @Resource
    private Fun1CostMapper fun1CostMapper;

    @Override
    public int newFun1Cost(Fun1Cost f) {
        return fun1CostMapper.insertNewCost(f);
    }
}
