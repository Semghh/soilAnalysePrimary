package com.example.content2;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.UUID;

public class Main {

    @Test
    public void test1() {
        System.out.println(1231);
        System.out.println(1231);
        System.out.println(1231);
        System.out.println(1231);
        System.out.println(1231);
        System.out.println(1231);
        System.out.println(1231);
        System.out.println(1231);
        System.out.println(1231);
        System.out.println(1231);
        System.out.println(1231);
        System.out.println(1231);
    }

    @Test
    public void test2(){
        System.out.println(123456);
        System.out.println(123456);
        System.out.println(123456);
        System.out.println(123456);
        System.out.println(123456);
        System.out.println(123456);
        System.out.println(123456);
        System.out.println(123456);
        System.out.println(123456);
        System.out.println(123456);
        System.out.println(123456);

    }

    @Test
    public void test3(){
        String str = "123456abc";
        String[] split = str.split("\\d*");
        System.out.println(Arrays.toString(split));
    }
    @Test
    public void test4(){
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
    }


}
