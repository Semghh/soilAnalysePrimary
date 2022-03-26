package com.example.content2.Config.BeanConfig;

import com.example.content2.Mapper.Primary.RegionMapper;
import com.example.content2.POJO.SoilAnalyse.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.Resource;
import java.util.List;

@Configuration
@DependsOn(value = {"regionMapper"})
public class RegionCacheBean {

    @Resource
    private RegionMapper regionMapper;

    @Value("${region.longitude.low}")
    private Double longitudeLow;

    @Value("${region.longitude.high}")
    private Double longitudeHigh;

    @Value("${region.latitude.low}")
    private Double latitudeLow;

    @Value("${region.longitude.high}")
    private Double latitudeHigh;

    @Bean("regionsCache")
    public List<Region> RegionsCache(){
        return regionMapper.selectOffsetRegion(longitudeLow,longitudeHigh,latitudeLow,latitudeHigh);
    }
}
