package com.example.content2.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpertSuggestValue {
    private Long id;
    private Double longitude;
    private Double latitude;
    private Integer element_Id;
    private Integer cropTypeId;
    private Double suggestValue;


    @Override
    public boolean equals(Object o){
        if (o instanceof ExpertSuggestValue){
            return this.id.equals(((ExpertSuggestValue) o).id);
        }
        return false;
    }
}
