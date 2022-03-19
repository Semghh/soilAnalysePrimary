package com.example.content2.POJO.SoilAnalyse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    private Integer id;
    private String username_;
    private String password_;
    private Integer peasant_id;
    private String roles;


    @Override
    public boolean equals(Object o){
        if (o instanceof Users){
            return this.id.equals(((Users) o).id);
        }
        return false;
    }
}
