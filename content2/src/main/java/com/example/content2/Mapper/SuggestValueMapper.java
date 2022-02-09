package com.example.content2.Mapper;

import com.example.content2.POJO.SuggestValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface SuggestValueMapper {
    @Select("select `result` from `suggest_value` where `crop_type`=#{crop_type} and `name_element`= #{name_element} and #{measured_value} > `min_value` and #{measured_value} <=`max_value`")
    public Double selectResult(@Param("crop_type") Integer crop_type, @Param("name_element") String name_element, @Param("measured_value") Double measured_value);

    public ArrayList<SuggestValue> getSuggestValuesByLimit(@Param("index") int index, @Param("size") int size);

    public int insertNewSuggestValue(SuggestValue s);

    public int getLatestId();

    public int dynamicUpdateSuggestValue(SuggestValue s);

    public int isExistId(Integer id);

    public int deleteById(Integer id);

    public int getSuggestValuesTotal();
}
