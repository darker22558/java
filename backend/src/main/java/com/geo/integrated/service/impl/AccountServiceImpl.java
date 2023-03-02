package com.geo.integrated.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geo.integrated.common.Result;
import com.geo.integrated.dao.SysUserMapper;
import com.geo.integrated.entity.SysUser;
import com.geo.integrated.model.dto.SysUserDetails;
import com.geo.integrated.service.AccountService;
import com.geo.integrated.service.RedisService;
import com.geo.integrated.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author: whtli
 * @date: 2023/01/16
 * @description: 用户登录管理业务层实现
 */
@Service
@Slf4j
public class AccountServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements AccountService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${redis.key.expire.authCode}")
    private Long authCodeExpireSeconds;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * 生成验证码
     *
     * @return 验证码
     */
    @Override
    public Result generateAuthCode() {
        StringBuilder sysAuthCode = new StringBuilder();
        Random random = new Random();
        int len = 6;
        for (int i = 0; i < len; i++) {
            sysAuthCode.append(random.nextInt(10));
        }
        // 生成当前时刻时间戳
        String timeStamp = String.valueOf(System.currentTimeMillis());
        try {
            // 时间戳绑定验证码然后通过md5转换再存储到redis
            String uniqueLoginId = SecureUtil.md5(timeStamp + sysAuthCode);
            log.info("授权密钥 === {}", uniqueLoginId);
            redisService.set(uniqueLoginId, sysAuthCode.toString());
            redisService.expire(uniqueLoginId, authCodeExpireSeconds);

            Map<String, String> data = new LinkedHashMap<>();
            data.put("sysAuthCode", sysAuthCode.toString());
            data.put("uniqueLoginId", uniqueLoginId);
            return Result.success("生成验证码成功", data);
        } catch (Exception e) {
            return Result.fail("生成验证码失败");
        }
    }

    /**
     * 对输入的验证码进行校验
     *
     * @param uniqueLoginId 唯一登录标识
     * @param authCode      验证码
     * @return 验证码的校验结果
     */
    @Override
    public boolean verifyAuthCode(String uniqueLoginId, String authCode) {
        if (StrUtil.isEmpty(authCode)) {
            return false;
        }
        String realAuthCode = redisService.get(uniqueLoginId);
        boolean result = authCode.equals(realAuthCode);
        if (result) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 用户登录匹配
     * 查询是否有与当前表单中的用户名、密码匹配的用户信息
     * 若有则生成token并返回用户信息和token
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户信息和token信息
     */
    @Override
    public Map<String, Object> handleLogin(String username, String password) {
        try {
            QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
            wrapper.eq("username", username);
            SysUser user = getOne(wrapper);
            if (user == null) {
                throw new BadCredentialsException("用户不存在");
            }
            // UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            SysUserDetails userDetails = new SysUserDetails(user);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenUtils.generateTokenByUserDetails(userDetails);
            if (token == null) {
                log.warn("token生成失败 === {}", username);
                throw new RuntimeException("未知错误，请刷新后重新登陆");
            }
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("userInfo", user);
            data.put("token", token);
            data.put("tokenHead", tokenHead);
            return data;
        } catch (AuthenticationException e) {
            log.warn("登录异常 === {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
