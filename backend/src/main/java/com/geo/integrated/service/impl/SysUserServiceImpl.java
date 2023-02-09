package com.geo.integrated.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geo.integrated.common.Result;
import com.geo.integrated.dao.SysUserMapper;
import com.geo.integrated.entity.SysUser;
import com.geo.integrated.service.RedisService;
import com.geo.integrated.service.SysUserService;
import com.geo.integrated.utils.JwtTokenUtils;
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

import java.util.LinkedHashMap;
import java.util.Map;
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
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
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
            log.info("Authorization unique key ========== {}", uniqueLoginId);
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
            token = jwtTokenUtils.generateToken(userDetails);
        } catch (AuthenticationException e) {
            log.warn("登录异常");
        }
        return token;
    }
}
