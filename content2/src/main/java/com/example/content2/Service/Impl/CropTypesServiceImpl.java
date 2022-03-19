package com.example.content2.Service.Impl;

import com.example.content2.Mapper.Primary.CropTypesMapper;
import com.example.content2.Service.CropTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Vector;

@Service("CropTypesService")
public class CropTypesServiceImpl implements CropTypesService {


    private CropTypesMapper cropTypesMapper;

    @Autowired
    public CropTypesServiceImpl(CropTypesMapper cropTypesMapper) {
        this.cropTypesMapper = cropTypesMapper;
    }



    @Override
    public int insert(CropTypesMapper ct) {
        return cropTypesMapper.insert(ct);
    }

    @Override
    public int delete(CropTypesMapper ct) {
        return cropTypesMapper.delete(ct);

    }

    @Override
    public int update(CropTypesMapper ct) {
        return cropTypesMapper.update(ct);
    }

    @Override
    public CropTypesMapper select(CropTypesMapper ct) {
        return cropTypesMapper.select(ct);
    }

    @Override
    public Integer getTypeIdByName(String typeName) {
        return cropTypesMapper.getTypeIdByName(typeName);
    }

    @Override
    public String getNameByTypeId(Integer id) {
        return cropTypesMapper.getNameByTypeId(id);
    }

    @Override
    public Vector<String> selectAllCropNames() {
        return cropTypesMapper.selectAllCropName();
    }

    @Override
    public Vector<String> getAllEnableCropName() {
        return cropTypesMapper.getAllEnableCropTypeName();
    }
}
