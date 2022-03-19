package com.example.content2.Service.Impl;

import com.example.content2.Mapper.Primary.QueryKeepTimeMapper;
import com.example.content2.POJO.SoilAnalyse.QueryKeepTime;
import com.example.content2.Service.QueryKeepTimeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("QueryKeepTimeService")
public class QueryKeepTimeServiceImpl implements QueryKeepTimeService {

    @Resource
    private QueryKeepTimeMapper queryKeepTimeMapper;

    @Override
    public int createNewRecord(QueryKeepTime record) {
        return queryKeepTimeMapper.insertNewRecord(record);
    }
}
