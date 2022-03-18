package com.example.content2.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryKeepTime {

    private Long id;
    private Long time_millis;
    private Long query_keep_time;
    private String signature;
    private String query_name;
    private String params;





    @Override
    public boolean equals(Object o){
        if (o instanceof QueryKeepTime){
            return this.id.equals(((QueryKeepTime) o).id);
        }
        return false;
    }

}
