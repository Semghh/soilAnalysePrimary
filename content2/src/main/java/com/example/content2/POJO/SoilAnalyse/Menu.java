package com.example.content2.POJO.SoilAnalyse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {

    private Integer id;
    private String name; //菜单名字
    private String url; //菜单url
    private String icon; //图标名称
    private Menu[] children;
    private Integer type;

    static public Integer[] convertChildStringToIdArray(String s){
        if (s==null)return null;
        String[] split = s.split(",");
        Integer[] res = new Integer[split.length];
        for (int i = 0; i < split.length; i++) {
            res[i] = Integer.parseInt(split[i]);
        }
        return res;
    }

    static public String convertChildrenArrayToString(Menu m){
        StringBuilder sb  = new StringBuilder();
        for (Menu child : m.children) {
            sb.append(child.id);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Menu){
            return this.id.equals(((Menu) o).id);
        }
        return false;
    }

}
