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

}
