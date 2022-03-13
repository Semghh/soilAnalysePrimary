package com.example.content2.Mapper;

import com.example.content2.POJO.QueryKeepTime;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QueryKeepTimeMapper {

    int insertNewRecord(QueryKeepTime record);

}
