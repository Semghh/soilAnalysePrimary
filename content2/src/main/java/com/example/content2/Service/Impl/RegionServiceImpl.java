package com.example.content2.Service.Impl;

import com.example.content2.Mapper.Primary.RegionMapper;
import com.example.content2.POJO.SoilAnalyse.Region;
import com.example.content2.Service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("RegionService")
public class RegionServiceImpl implements RegionService {

    private final RegionMapper regionMapper;

    @Autowired
    public RegionServiceImpl(RegionMapper regionMapper) {
        this.regionMapper = regionMapper;
    }

    @Override
    public int delete(Region region) {
        return regionMapper.delete(region);
    }

    @Override
    public Region selectById_county(Region region) {
        return regionMapper.selectById_county(region);
    }

    @Override
    public Integer getLastestNumberFromRegion() {
        return regionMapper.getLastestNumberFromRegion();
    }

    @Override
    public int insertPartly(Region r) {
        return regionMapper.insertPartly(r);
    }

    @Override
    public Region getRegionByLongitudeAndLatitude(Double longitude, Double latitude) {
        return regionMapper.getRegionByLongitudeAndLatitude(longitude,latitude);
    }

    @Override
    public ArrayList<Region> selectOffsetRegion(Double longitude_low, Double longitude_high, Double latitude_low, Double latitude_high) {
        return regionMapper.selectOffsetRegion(longitude_low,longitude_high,latitude_low,latitude_high);
    }

    @Override
    public int updatePartly(Region r) {
        return regionMapper.updatePartly(r);
    }

    @Override
    public boolean isExistRegionByLatLong(Double longitude, Double latitude) {
        return regionMapper.existsRegion(longitude,latitude)==1;
    }

}
