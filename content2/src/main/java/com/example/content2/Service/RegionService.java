package com.example.content2.Service;

import com.example.content2.POJO.Region;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

public interface RegionService {

    public int delete(Region region);

    public Region selectById_county(Region region);

    public Integer getLastestNumberFromRegion();

    public int insertPartly(Region r);

    public Region getRegionByLongitudeAndLatitude(Double Longitude, Double Latitude);

    public ArrayList<Region> selectOffsetRegion(Double longitude_low, Double longitude_high, Double latitude_low,
                                                Double latitude_high);
    public int updatePartly(Region r);

    public boolean isExistRegionByLatLong(Double longitude,Double latitude);
}
