package com.geo.integrated.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geo.integrated.common.Result;
import com.geo.integrated.dao.SysUserMapper;
import com.geo.integrated.entity.SysUser;
import com.geo.integrated.service.RedisService;
import com.geo.integrated.service.SysUserService;
import com.geo.integrated.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Random;

/**
 * @author: whtli
 * @date: 2023/01/16
 * @description: 用户相关功能服务层实现
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private RedisService redisService;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${redis.key.prefix.authCode}")
    private String redisKeyPrefixAuthCode;

    @Value("${redis.key.expire.authCode}")
    private Long authCodeExpireSeconds;

    /**
     * 用户登录匹配
     * 查询是否有与当前表单中的用户名、密码匹配的用户信息
     *
     * @param username 用户名
     * @param password 密码
     * @return 匹配成功返回用户实体类，失败返回null
     */
    @Override
    public SysUser login(String username, String password) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (userDetails == null) {
                return null;
            }
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
            wrapper.eq("username", username);
            SysUser user = getOne(wrapper);
            return user;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 生成验证码
     *
     * @param username 用户名
     * @return 验证码
     */
    @Override
    public Result generateAuthCode(String username) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int len = 6;
        for (int i = 0; i < len; i++) {
            sb.append(random.nextInt(10));
        }
        try {
            // 验证码绑定用户名并存储到redis
            redisService.set(redisKeyPrefixAuthCode + username, sb.toString());
            redisService.expire(redisKeyPrefixAuthCode + username, authCodeExpireSeconds);
            return Result.success("生成验证码成功", sb.toString());
        } catch (Exception e) {
            return Result.fail("生成验证码失败");
        }
    }

    /**
     * 对输入的验证码进行校验
     *
     * @param username 用户名
     * @param authCode 验证码
     * @return 验证码的校验结果
     */
    @Override
    public boolean verifyAuthCode(String username, String authCode) {
        if (StrUtil.isEmpty(authCode)) {
            return false;
        }
        String realAuthCode = redisService.get(redisKeyPrefixAuthCode + username);
        boolean result = authCode.equals(realAuthCode);
        if (result) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 生成token
     *
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    @Override
    public String generateToken(String username, String password) {
        String token = null;
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            log.warn("登录异常");
        }
        return token;
    }
}
