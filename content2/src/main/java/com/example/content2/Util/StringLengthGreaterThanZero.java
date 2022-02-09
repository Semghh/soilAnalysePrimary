package com.example.content2.Util;

public class StringLengthGreaterThanZero {
    public static boolean judge(String... strings){
        for (String string : strings) {
            if (string==null || string.length()==0)
                return false;
        }
        return true;
    }
}
