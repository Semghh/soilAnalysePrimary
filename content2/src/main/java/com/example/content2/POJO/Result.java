package com.example.content2.POJO;

import lombok.Data;

@Data
public class Result {
    private int code;
    private String msg;
    private Object data;

    private Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result getInstance(int code, String msg, Object data) {
        return new Result(code, msg, data);
    }
}
