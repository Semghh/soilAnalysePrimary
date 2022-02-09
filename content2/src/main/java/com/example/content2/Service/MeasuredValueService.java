package com.example.content2.Service;

import com.example.content2.POJO.MeasuredValue;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface MeasuredValueService {


    public int insert(MeasuredValue mv);


    public int delete(MeasuredValue mv);



    public MeasuredValue select(MeasuredValue mv);


    public MeasuredValue selectById_village(Long id_village);


    public int insertPartly(MeasuredValue mv);
}
