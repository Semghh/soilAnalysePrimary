package com.example.content2.POJO.SoilAnalyse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Region {
    private Long id;  //编号
    private Long id_county;  //点县内编号
    private String name_county; //县名称
    private String name_countryside; // 乡 或 镇名称
    private String name_village; //村名称
    private Double longitude;//经度
    private Double latitude;//纬度


    @Override
    public boolean equals(Object o){
        if (o instanceof Region){
            return this.id.equals(((Region) o).id);
        }
        return false;
    }
}
