package com.example.content2.Mapper;

import com.example.content2.POJO.MeasuredValue;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MeasuredValueMapper {
    @Insert("insert into measured_value values (#{id_village},#{ph},#{organic_matter},#{total_nitrogen},#{Olsen_P},#{Olsen_K},#{slowly_K},#{Effective_Cu},#{Effective_Zn},#{Effective_Fe},#{Effective_Mn})")
    public int insert(MeasuredValue mv);
    
    @Delete("delete from measured_value where id_village=#{id_village}")
    public int delete(MeasuredValue mv);

    @Select("select * from measured_value where id_village=#{id_village}")
    public MeasuredValue select(MeasuredValue mv);

    @Select("select * from measured_value where id_village=#{id}")
    public MeasuredValue selectById_village(Long id_village);

    @Insert("insert into measured_value(id_village,ph,organic_matter,Olsen_P,Olsen_K,Effective_N) values (#{id_village},#{ph},#{organic_matter},#{Olsen_P},#{Olsen_K},#{Effective_N})")
    public int insertPartly(MeasuredValue mv);
}
