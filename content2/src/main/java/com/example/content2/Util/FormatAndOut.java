package com.example.content2.Util;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Vector;

public class FormatAndOut {

    /**
     *
     *  工具类,用于 规格化返回参数
     *
     */


    @Deprecated
    public void formatAndOut(Map map, Vector<String> names, Double... elements) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] != null) {
                String format = decimalFormat.format(elements[i]);
                String name = names.get(i);
                map.put(name, format);
//                System.out.println(name + " : " + format);
            } else {
                String name = names.get(i);
                map.put(name, "无参考值");
//                System.out.println(name + "无参考值");
            }
        }
    }

    /**
     *
     * fun1 使用
     *
     *
     * @param map  Response 中返回data的容器
     * @param names 需求的目标元素
     * @param meaValue  测量值数组
     * @param sugValue  建议值数组
     *
     * Since:   content2
     */
    public void formatAndOut(Map map, Vector<String> names, Double[] meaValue,Double[] sugValue) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        for (int i = 0; i < names.size(); i++) {
            if (meaValue[i] != null) {
                String format = decimalFormat.format(meaValue[i]);
                map.put("mea_"+names.get(i), format);
            } else {
                map.put("mea_"+names.get(i), "无参考值");
            }
            if(sugValue[i]!=null){
                String format = decimalFormat.format(sugValue[i]);
                map.put("sug_"+names.get(i),format);
            }else {
                map.put("sug_"+names.get(i), "无参考值");
            }
        }
    }

    /**
     *
     *  fun2使用
     *
     *  Since: content1
     */
    public void formatAndOut(Map map, Vector<String> names, Vector<Integer> flags, Double... elements) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        for (int i = 0; i < elements.length; i++) {
            if (flags.get(i) != 0 && elements[i] != null) {
                String format = decimalFormat.format(elements[i]);
                String name = names.get(i);
                map.put(name, format);
            } else {
                String name = names.get(i);
                map.put(name, "无参考值");
            }
        }
    }
}
