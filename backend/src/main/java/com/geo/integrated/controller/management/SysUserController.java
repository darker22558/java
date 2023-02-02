package com.geo.integrated.controller.management;

import com.geo.integrated.common.Result;
import com.geo.integrated.model.dto.LoginDTO;
import com.geo.integrated.entity.SysUser;
import com.geo.integrated.service.SysUserService;
import com.geo.integrated.utils.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/01/14
 * @description: 用户管理控制层
 */
@Api(tags = "SysUserController")
@Slf4j
@RestController
@RequestMapping("/management/system/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 用户登录
     *
     * @param loginDTO 登录DTO，包含用户名和密码信息
     * @return 登录成功返回用户信息和jwt，登录失败返回错误信息
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDTO loginDTO) {
        log.info("login DTO =========== {}", loginDTO);
        SysUser user = sysUserService.login(loginDTO);
        if (user == null) {
            return Result.fail("用户不存在或密码不正确");
        }
        Result result = sysUserService.verifyAuthCode(loginDTO.getUsername(), loginDTO.getAuthCode());
        if (result.getCode() != 200) {
            return Result.fail("验证码校验失败，请刷新");
        }
        String jwt = TokenUtils.generateToken(user.getId(), user.getPassword());
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("user", user);
        data.put("jwt", jwt);
        return Result.success("登录成功", data);
    }

    /**
     * 用户退出
     *
     * @return 成功退出登录的信息
     */
    @ApiOperation("用户退出")
    @PostMapping("/logout")
    public Result logout() {
        return Result.success("退出登录");
    }

    /**
     * 获取验证码
     *
     * @param username 用户名
     * @return 验证码
     */
    @ApiOperation("生成验证码")
    @GetMapping("/generateAuthCode")
    public Result generateAuthCode(@RequestParam String username) {
        return sysUserService.generateAuthCode(username);
    }
}
