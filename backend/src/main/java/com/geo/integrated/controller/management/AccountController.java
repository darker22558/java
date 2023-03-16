package com.geo.integrated.controller.management;

import com.geo.integrated.common.Constant;
import com.geo.integrated.common.Result;
import com.geo.integrated.model.dto.LoginDTO;
import com.geo.integrated.service.AccountService;
import com.geo.integrated.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/02/14
 * @description: 用户登录和退出控制层
 */
@Api(tags = "AccountController")
@Slf4j
@RestController
@RequestMapping("/management/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private JwtUtils jwtUtils;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * 用户登录
     *
     * @param loginDTO 登录DTO，包含用户名和密码信息
     * @return 登录成功返回用户信息和jwt，登录失败返回错误信息
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDTO loginDTO) {
        log.info("Login info === {}", loginDTO);
        // 校验登录验证码
        boolean isVerified = accountService.verifyAuthCode(loginDTO.getUniqueLoginId(), loginDTO.getAuthCode());
        if (!isVerified) {
            return Result.fail("验证码校验失败");
        }
        // 校验登录信息
        Map<String, Object> loginData = accountService.handleLogin(loginDTO.getUsername(), loginDTO.getPassword());
        // 返回登录结果
        if (loginData != null) {
            return Result.success("登录成功！", loginData);
        }
        return Result.fail("登录失败，请刷新后重试！");
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
     * @return 验证码
     */
    @ApiOperation("生成验证码")
    @GetMapping("/generateAuthCode")
    public Result generateAuthCode() {
        return accountService.generateAuthCode();
    }

    /**
     * 判断token是否需要刷新
     *
     * @param token token
     * @return token是否需要刷新的判断结果
     */
    @ApiOperation(value = "判断token是否过期")
    @GetMapping("/isTokenNeedToBeRefreshed")
    public Result isTokenNeedToBeRefreshed(@RequestParam String token) {
        log.info("判断token是否需要刷新 === {}", token);
        boolean flag = jwtUtils.isTokenNeedToBeRefreshed(token);
        return Result.success(flag);
    }

    /**
     * 刷新token
     *
     * @param request 请求头
     * @return token信息
     */
    @ApiOperation(value = "刷新token")
    @GetMapping("/refreshToken")
    public Result refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshToken = jwtUtils.refreshHeadToken(token);
        if (refreshToken == null) {
            return Result.fail(Constant.CODE_UNAUTHORIZED,"token无效！", null);
        }
        Map<String, String> tokenMap = new LinkedHashMap<>();
        tokenMap.put("token", refreshToken);
        tokenMap.put("tokenHead", tokenHead);
        return Result.success(tokenMap);
    }
}
