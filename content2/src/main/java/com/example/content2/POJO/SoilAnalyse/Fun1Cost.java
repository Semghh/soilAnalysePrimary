package com.example.content2.POJO.SoilAnalyse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fun1Cost {

    private Long id;
    private Long time_millis;
    private Long keep_time;

    @Override
    public boolean equals(Object o){
        if (o instanceof Fun1Cost){
            return this.id.equals(((Fun1Cost) o).id);
        }
        return false;
    }
}
