package com.geo.integrated.controller;

import com.geo.integrated.annotation.OperationLogger;
import com.geo.integrated.common.Result;
import com.geo.integrated.service.TestService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: whtli
 * @date: 2023/01/13
 * @description: 测试接口
 */
@Api(tags = "TestController", description = "测试接口")
@RestController
@RequestMapping("/management/test")
public class TestController {
    @Autowired
    private TestService testService;
    @OperationLogger("测试连接")
    @GetMapping("/hello")
    public Result test() {
        String testString = "hello world";
        // testService.testGlobalException(testString);
        return Result.success("连接成功", testString);
    }
}
