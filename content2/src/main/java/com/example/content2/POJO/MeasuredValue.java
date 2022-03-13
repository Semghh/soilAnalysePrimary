package com.example.content2.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeasuredValue {

    private Long id_village;
    private Double ph;
    private Double organic_matter;
    private Double total_nitrogen;
    private Double Olsen_P;
    private Double Olsen_K;
    private Double slowly_K;
    private Double Effective_Cu;
    private Double Effective_Zn;
    private Double Effective_Fe;
    private Double Effective_Mn;
    private Double Effective_N;


    @Override
    public boolean equals(Object o){
        if (o instanceof MeasuredValue){
            return this.id_village.equals(((MeasuredValue) o).id_village);
        }
        return false;
    }
}
