package com.example.content2.POJO.SoilAnalyse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Peasant {
     private Long id;
     private String pName;
     private String id_card;


     @Override
     public boolean equals(Object o){
          if (o instanceof Peasant){
               return this.id.equals(((Peasant) o).id);
          }
          return false;
     }

}
