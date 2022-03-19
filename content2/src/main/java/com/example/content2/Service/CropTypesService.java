package com.example.content2.Service;


import com.example.content2.Mapper.Primary.CropTypesMapper;

import java.util.Vector;

public interface CropTypesService {

    public int insert(CropTypesMapper ct);


    public int delete(CropTypesMapper ct);

    public int update(CropTypesMapper ct);


    public CropTypesMapper select(CropTypesMapper ct);


    public Integer getTypeIdByName(String typeName);


    public String getNameByTypeId(Integer id);

    public Vector<String> selectAllCropNames();

    public Vector<String> getAllEnableCropName();

}
