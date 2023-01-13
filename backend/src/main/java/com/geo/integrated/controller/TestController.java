package com.geo.integrated.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: whtli
 * @date: 2023/01/13
 * @description: 测试接口
 */
@RestController
public class TestController {
    //
    @GetMapping("/")
    public String test() {
        String str1 = "Hello World!!!!!";
        return str1;
    }
}
