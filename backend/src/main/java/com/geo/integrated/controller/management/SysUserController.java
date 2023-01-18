package com.geo.integrated.controller.management;

import com.geo.integrated.common.Result;
import com.geo.integrated.model.dto.LoginDTO;
import com.geo.integrated.model.entity.User;
import com.geo.integrated.service.UserService;
import com.geo.integrated.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/01/14
 * @description: 用户管理
 */
@RestController
@RequestMapping("/management/user")
public class SysUserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDTO loginDTO) {
        User user = userService.login(loginDTO);
        if (user == null) {
            return Result.fail("用户不存在或密码不正确");
        }
        String jwt = TokenUtils.generateToken(user.getId(), user.getPassword());
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("user", user);
        data.put("jwt", jwt);
        return Result.success("登录成功", data);
    }
    @PostMapping("/logout")
    public Result logout() {
        return Result.success("退出登录");
    }
}
