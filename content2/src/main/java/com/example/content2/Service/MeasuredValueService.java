package com.example.content2.Service;

import com.example.content2.POJO.SoilAnalyse.MeasuredValue;

public interface MeasuredValueService {


    public int insert(MeasuredValue mv);


    public int delete(MeasuredValue mv);



    public MeasuredValue select(MeasuredValue mv);


    public MeasuredValue selectById_village(Long id_village);


    public int insertPartly(MeasuredValue mv);
}
