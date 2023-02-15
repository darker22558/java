package com.geo.integrated.common;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author: whtli
 * @date: 2023/01/14
 * @description: 接口统一返回包装类
 */

@Data
@ToString
public class Result implements Serializable {
    private Integer code;
    private String message;
    private Object data;

    public static Result success(Object data) {
        return success(Constant.CODE_SUCCESS, "请求成功", data);
    }

    public static Result success(String message, Object data) {
        return success(Constant.CODE_SUCCESS, message, data);
    }

    public static Result success(Integer code, String message, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    public static Result fail(String message) {
        return fail(Constant.CODE_BAD_REQUEST, message, null);
    }

    public static Result fail(String message, Object data) {
        return fail(Constant.CODE_BAD_REQUEST, message, data);
    }

    public static Result fail(Integer code, String message, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMessage(message);
        r.setData(data);
        return r;
    }
}
