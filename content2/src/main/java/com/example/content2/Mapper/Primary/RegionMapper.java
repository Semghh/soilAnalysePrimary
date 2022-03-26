package com.example.content2.Mapper.Primary;

import com.example.content2.Annotation.MonitorQuery;
import com.example.content2.POJO.SoilAnalyse.Region;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;


public interface RegionMapper {
//    public void insert(Region region);

    @Delete("delete from region where id=#{id}")
    public int delete(Region region);

//    public void update(Region region);

    @Select("select * from region where id_county = #{id_county}")
    public Region selectById_county(Region region);

    @Select("select id from region order by id desc limit 1")
    public Integer getLastestNumberFromRegion();

    @Insert("insert into region(id,name_countryside,name_village,longitude,latitude) values(#{id},#{name_countryside},#{name_village},#{longitude},#{latitude})")
    public int insertPartly(Region r);

    @Select("select * from region where longitude=#{longitude} and latitude=#{latitude}")
    public Region getRegionByLongitudeAndLatitude(@Param("longitude") Double Longitude, @Param("latitude") Double Latitude);

    @MonitorQuery
    public ArrayList<Region> selectOffsetRegion(@Param("longitude_low") Double longitude_low,
                                                @Param("longitude_high") Double longitude_high,
                                                @Param("latitude_low") Double latitude_low,
                                                @Param("latitude_high") Double latitude_high);

    @Update("update region set longitude=#{longitude},latitude=#{latitude} where id =#{id}")
    public int updatePartly(Region r);


    public int existsRegion(@Param("longitude") Double longitude,@Param("latitude") Double latitude);

    public ArrayList<Region> selectByName_county(String s);
}
