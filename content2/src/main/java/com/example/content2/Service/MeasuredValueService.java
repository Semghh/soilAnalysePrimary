package com.example.content2.Service;

import com.example.content2.POJO.SoilAnalyse.MeasuredValue;

public interface MeasuredValueService {


     int insert(MeasuredValue mv);


     int delete(MeasuredValue mv);



     MeasuredValue select(MeasuredValue mv);


     MeasuredValue selectById_village(Long id_village);


     int insertPartly(MeasuredValue mv);
}
