package com.example.content2.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Element {
    private Integer id;
    private String expression_;
    private String translation;


    @Override
    public boolean equals(Object o){
        if (o instanceof Element){
            return this.id.equals(((Element) o).id);
        }
        return false;
    }
}
