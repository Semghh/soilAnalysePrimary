package com.example.content2.Mapper.Secondary;

import com.example.content2.POJO.fun1Calculate.CalculateRecord;
import org.apache.ibatis.annotations.Param;

public interface CalculateMapper {

    int createCalculateTable(String tableName);

    int insertNewCalculateRecord(@Param("tableName") String tableName,
                                 @Param("cr") CalculateRecord cr);


    String showTable(String tableName);
}
