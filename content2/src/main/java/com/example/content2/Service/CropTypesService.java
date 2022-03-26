package com.example.content2.Service;


import com.example.content2.Mapper.Primary.CropTypesMapper;

import java.util.Vector;

public interface CropTypesService {

    int insert(CropTypesMapper ct);


    int delete(CropTypesMapper ct);

    int update(CropTypesMapper ct);


    CropTypesMapper select(CropTypesMapper ct);


    Integer getTypeIdByName(String typeName);


    String getNameByTypeId(Integer id);

    Vector<String> selectAllCropNames();

    Vector<String> getAllEnableCropName();

}
