package com.example.content2.Util;

import java.util.Locale;

public class CheckId_cardLegality {
    public static boolean judge(String id_card){
        //不满足18位
        id_card=id_card.toUpperCase(Locale.ROOT);
        if (id_card.length()!=18){
            System.out.println("长度不匹配");
            return false;
        }
        if (!id_card.matches("[0-9]{18}|[0-9]{17}[X]")) {
            System.out.println("不是18纯数字 或 17+x/X形式的");
            return false;
        }
        char[] chars = id_card.toCharArray();
        int sum=0;
        sum+=(chars[0]-'0')*7;
        sum+=(chars[1]-'0')*9;
        sum+=(chars[2]-'0')*10;
        sum+=(chars[3]-'0')*5;
        sum+=(chars[4]-'0')*8;
        sum+=(chars[5]-'0')*4;
        sum+=(chars[6]-'0')*2;
        sum+=(chars[7]-'0');
        sum+=(chars[8]-'0')*6;
        sum+=(chars[9]-'0')*3;
        sum+=(chars[10]-'0')*7;
        sum+=(chars[11]-'0')*9;
        sum+=(chars[12]-'0')*10;
        sum+=(chars[13]-'0')*5;
        sum+=(chars[14]-'0')*8;
        sum+=(chars[15]-'0')*4;
        sum+=(chars[16]-'0')*2;

        int res = sum%11;
        int replication=-1;

        switch (res){
            case 0:{
                replication=1;
                break;
            }
            case 1:{
                replication=0;
                break;
            }
            case 2:{
                replication=(int)'X';
                break;
            }
            case 3:{
                replication=9;
                break;
            }
            case 4:{
                replication=8;
                break;
            }
            case 5:{
                replication=7;
                break;
            }
            case 6:{
                replication=6;
                break;
            }
            case 7:{
                replication=5;
                break;
            }
            case 8:{
                replication=4;
                break;
            }
            case 9:{
                replication=3;
                break;
            }
            case 10:{
                replication=2;
                break;
            }
        }
        if (replication==(chars[17]-'0'))
            return true;
        if (replication=='X' && chars[17]=='X'){
            return true;
        }
        return false;

    }
}
