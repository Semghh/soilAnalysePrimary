package com.example.content2.POJO.fun1Calculate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculateRecord {

    private Long id;
    private Long database_date;
    private Long calculate_date;
    private Double longitude;
    private Double latitude;
    private Double offset;
    private Integer magnification;
    private Long response;
    private Integer append_time;
    private Integer is_direct;
    private Integer region_size;


}
