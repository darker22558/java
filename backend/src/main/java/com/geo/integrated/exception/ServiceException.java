package com.geo.integrated.exception;

import lombok.Data;

/**
 * @author: whtli
 * @date: 2023/01/28
 * @description: 自定义异常，ServiceImpl层的异常可以调用
 */
@Data
public class ServiceException extends RuntimeException{
    private Integer code;
    public ServiceException (Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}
