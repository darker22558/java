package com.geo.integrated.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geo.integrated.common.Result;
import com.geo.integrated.dao.SysUserMapper;
import com.geo.integrated.model.dto.LoginDTO;
import com.geo.integrated.entity.SysUser;
import com.geo.integrated.service.RedisService;
import com.geo.integrated.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Random;

/**
 * @author: whtli
 * @date: 2023/01/16
 * @description: 用户相关功能服务层实现
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private RedisService redisService;

    @Value("${redis.key.prefix.authCode}")
    private String redisKeyPrefixAuthCode;

    @Value("${redis.key.expire.authCode}")
    private Long authCodeExpireSeconds;

    /**
     * 用户登录匹配
     * 查询是否有与当前表单中的用户名、密码匹配的用户信息
     *
     * @param loginDTO 登录DTO，包含用户名和密码信息
     * @return 匹配成功返回用户实体类，失败返回null
     */
    @Override
    public SysUser login(LoginDTO loginDTO) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<SysUser>();
        wrapper.eq("username", loginDTO.getUsername());
        // 密码通过md5加密后再判断
        String md5Password = SecureUtil.md5(loginDTO.getPassword());
        wrapper.eq("password", md5Password);
        SysUser one = getOne(wrapper);
        if (one != null) {
            return one;
        } else {
            return null;
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
    public Result verifyAuthCode(String username, String authCode) {
        if (StrUtil.isEmpty(authCode)) {
            return Result.fail("请输入验证码");
        }
        String realAuthCode = redisService.get(redisKeyPrefixAuthCode + username);
        boolean result = authCode.equals(realAuthCode);
        if (result) {
            return Result.success("验证码校验成功");
        } else {
            return Result.fail("验证码校验失败");
        }
    }
}
