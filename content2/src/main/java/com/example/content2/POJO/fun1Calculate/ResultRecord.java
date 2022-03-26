package com.example.content2.POJO.fun1Calculate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultRecord {

    private Long id;
    private Long database_date;
    private Long calculate_date;
    private Double offset;
    private Integer magnification;
    private Double average_append_times;
    private Long average_response;
    private Long loop_times;
    private Long average_region_size;



}
