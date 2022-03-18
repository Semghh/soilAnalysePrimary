package com.example.content2.Mapper;

import com.example.content2.POJO.Fun1Cost;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Fun1CostMapper {
    int insertNewCost(Fun1Cost fun1Cost);
}
