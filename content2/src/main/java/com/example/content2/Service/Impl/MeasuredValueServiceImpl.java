package com.example.content2.Service.Impl;

import com.example.content2.Mapper.Primary.MeasuredValueMapper;
import com.example.content2.POJO.SoilAnalyse.MeasuredValue;
import com.example.content2.Service.MeasuredValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("MeasuredValueService")
public class MeasuredValueServiceImpl implements MeasuredValueService {

    private final MeasuredValueMapper measuredValueMapper;

    @Autowired
    public MeasuredValueServiceImpl(MeasuredValueMapper measuredValueMapper) {
        this.measuredValueMapper = measuredValueMapper;
    }

    @Override
    public int insert(MeasuredValue mv) {
        return measuredValueMapper.insert(mv);
    }

    @Override
    public int delete(MeasuredValue mv) {
        return measuredValueMapper.delete(mv);
    }

    @Override
    public MeasuredValue select(MeasuredValue mv) {
        return measuredValueMapper.select(mv);
    }

    @Override
    public MeasuredValue selectById_village(Long id_village) {
        return measuredValueMapper.selectById_village(id_village);
    }

    @Override
    public int insertPartly(MeasuredValue mv) {
        return measuredValueMapper.insertPartly(mv);
    }
}
