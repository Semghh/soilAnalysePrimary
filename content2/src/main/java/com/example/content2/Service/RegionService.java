package com.example.content2.Service;

import com.example.content2.POJO.SoilAnalyse.Region;

import java.util.ArrayList;

public interface RegionService {

     int delete(Region region);

     Region selectById_county(Region region);

     Integer getLastestNumberFromRegion();

     int insertPartly(Region r);

     Region getRegionByLongitudeAndLatitude(Double Longitude, Double Latitude);

     ArrayList<Region> selectOffsetRegion(Double longitude_low, Double longitude_high, Double latitude_low,
                                                Double latitude_high);
     int updatePartly(Region r);

     boolean isExistRegionByLatLong(Double longitude,Double latitude);
}
