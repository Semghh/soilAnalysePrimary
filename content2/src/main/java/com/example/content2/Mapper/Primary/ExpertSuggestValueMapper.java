package com.example.content2.Mapper.Primary;

import com.example.content2.POJO.SoilAnalyse.ExpertSuggestValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface ExpertSuggestValueMapper {

    public Double getExpertSuggestValue(@Param("lon") Double longitude, @Param("lat") Double latitude,
                                        @Param("ele") Integer elementId, @Param("cro") Integer cropTypeId);

    public int insertNewExpertSuggestValue(ExpertSuggestValue expertSuggestValue);

    public int existSuggestValue(@Param("lon") Double longitude, @Param("lat") Double latitude,
                                 @Param("ele") Integer elementId, @Param("cro") Integer cropTypeId);

    public int existSuggestValueByBean(ExpertSuggestValue expertSuggestValue);

    public int updateSuggestValue(@Param("lon") Double longitude, @Param("lat") Double latitude,
                                  @Param("ele") Integer elementId, @Param("cro") Integer cropTypeId,@Param("val") Double suggestValue);

    public int updateSuggestValueByBean(ExpertSuggestValue expertSuggestValue);

    public ArrayList<ExpertSuggestValue> getExpertSuggestValueByLimit(@Param("index") int index,@Param("size") int size);

    public int deleteById(Integer id);

    public int getTotal();
}
