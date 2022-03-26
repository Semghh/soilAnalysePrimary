package com.example.content2.Mapper.Secondary;

import com.example.content2.POJO.fun1Calculate.ResultRecord;
import org.apache.ibatis.annotations.Mapper;


public interface RecordMapper {

    int insertNewResultRecord(ResultRecord cr);

}
