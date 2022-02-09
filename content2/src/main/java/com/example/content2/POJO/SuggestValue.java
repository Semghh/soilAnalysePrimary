package com.example.content2.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuggestValue {
    private Integer id;
    private Integer crop_type;
    private String name_element;
    private Double min_value;
    private Double max_value;
    private Double result;
}
