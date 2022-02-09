package com.example.content2.Util;

public class DoubleFormat {

    public static String format(int postNumber,Double source){
        int exp = 1;
        for (int i = 0; i < postNumber; i++) {
            exp *=10;
        }
        double d = (double) Math.round(source*exp)/100;
        return new Double(d).toString();
    }
}
