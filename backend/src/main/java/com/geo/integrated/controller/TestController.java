package com.geo.integrated.controller;

import com.geo.integrated.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: whtli
 * @date: 2023/01/13
 * @description: 测试接口
 */
@RestController
@RequestMapping("/management/test")
public class TestController {
    @GetMapping("/hello")
    public Result test() {
        String testString = "hello world";
        return Result.success("连接成功", testString);
    }
}
