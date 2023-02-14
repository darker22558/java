package com.geo.integrated.exception;

import com.alibaba.druid.sql.visitor.functions.Concat;
import com.geo.integrated.common.Constant;
import com.geo.integrated.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author: whtli
 * @date: 2023/01/28
 * @description: 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 实体校验异常
     *
     * @param e 前端传入的实体不满足@Validate规则触发异常
     * @return 异常信息
     */
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e) {
        log.error("实体校验异常 === {}", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        /*return Result.fail(objectError.getDefaultMessage());*/
        return Result.fail(Constant.CODE_BAD_REQUEST, "实体校验异常", null);
    }

    /**
     * 运行时异常
     *
     * @param e 运行时异常
     * @return 异常信息
     */
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException e) {
        log.error("运行时异常 === {}", e.getMessage());
        /*return Result.fail(e.getMessage());*/
        return Result.fail(Constant.CODE_SYSTEM_ERROR, e.getMessage(), null);
    }

    /**
     * 如果抛出的是ServiceException，则调用该方法
     *
     * @param se 服务层实现异常
     * @return 异常信息
     */
    @ResponseBody
    @ExceptionHandler(ServiceException.class)
    public Result handler(ServiceException se) {
        return Result.fail(se.getCode(), se.getMessage(), null);
    }
}
