package com.example.content2.Mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Vector;

@Mapper
public interface CropTypesMapper {
    public int insert(CropTypesMapper ct);

    @Delete("delete from crop_types where id=#{id}")
    public int delete(CropTypesMapper ct);

    public int update(CropTypesMapper ct);

    @Select("select * from crop_types where id=#{id}")
    public CropTypesMapper select(CropTypesMapper ct);

    @Select("select id from crop_types where crop_name=#{typeName}")
    public Integer getTypeIdByName(String typeName);

    @Select("select crop_name from crop_types where id=#{id}")
    public String getNameByTypeId(Integer id);

    @Select("select crop_name from crop_types")
    public Vector<String> selectAllCropName();

    public Vector<String> getAllEnableCropTypeName();
}
