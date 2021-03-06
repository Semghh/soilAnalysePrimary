package com.example.content2.Mapper.Primary;

import com.example.content2.POJO.SoilAnalyse.Peasant;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


public interface PeasantMapper {
    @Select("select id from peasant where id_card=#{id_card}")
    public Integer getIdByIdCard(String id_card);

    @Insert("insert into peasant set pName=#{pName},id_card=#{id_card}")
    public int registerPeasant(Peasant p);

    @Select("select count(*) from peasant where id_card=#{id_card}")
    public int isExistsId_card(String id_card);

}
