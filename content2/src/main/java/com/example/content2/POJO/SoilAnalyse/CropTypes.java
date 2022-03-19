package com.example.content2.POJO.SoilAnalyse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CropTypes {
    private Integer id;
    private String crop_name;
    private Boolean enable;

    @Override
    public boolean equals(Object o){
        if (o instanceof CropTypes){
            return this.id.equals(((CropTypes) o).id);
        }
        return false;
    }

}
