package com.example.content2.Util;

import java.lang.reflect.Constructor;
import java.util.Map;

public class getFieldFromMap {

    static public Object[] get(Map map, String[]fieldNames, Class[] clz)
            throws notFoundSuchField{
        Object[] res = new Object[fieldNames.length];
        for (int i = 0; i < fieldNames.length; i++) {
            if (!map.containsKey(fieldNames[i])){
                throw new notFoundSuchField(fieldNames[i]);
            }
            String o = (String) map.get(fieldNames[i]);
            if (o==null){
                continue;
            }
            res[i]= getObject(o, clz[i]);
        }
        return res;
    }


    static private <T> Object getObject(String s, T t){
        try{
            if (t == Integer.class){
                Constructor<Integer> constructor = Integer.class.getConstructor(String.class);
                return constructor.newInstance(s);
            }
            else if (t == String.class){
                return s;
            }
            else if (t == Double.class){
                Class<Double> doubleClass = Double.class;
                Constructor<Double> constructor = doubleClass.getConstructor(String.class);
                return constructor.newInstance(s);
            }else if (t==Float.class){
                Constructor<Float> constructor = Float.class.getConstructor(String.class);
                return constructor.newInstance(s);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    static public class notFoundSuchField extends Exception{
        public notFoundSuchField(String message) {
            super("不存在这样的字段名: "+message);
        }
    }
}
