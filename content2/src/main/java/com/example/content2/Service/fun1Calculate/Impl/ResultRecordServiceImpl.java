package com.example.content2.Service.fun1Calculate.Impl;

import com.example.content2.Mapper.Secondary.RecordMapper;
import com.example.content2.POJO.fun1Calculate.CalculateRecord;
import com.example.content2.POJO.fun1Calculate.ResultRecord;
import com.example.content2.Service.fun1Calculate.ResultRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("ResultRecordService")
public class ResultRecordServiceImpl implements ResultRecordService {

    @Resource 
    private RecordMapper recordMapper;

    @Override
    public int insertNewResultRecord(ResultRecord rr) {
        return recordMapper.insertNewResultRecord(rr);
    }
}
