package com.example.content2.POJO.SoilAnalyse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuStoreTable {
    private Integer id;
    private String name; //菜单名字
    private String url; //菜单url
    private String icon; //图标名称
    private String children;
    private Integer type;


}
