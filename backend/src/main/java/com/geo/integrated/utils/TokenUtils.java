package com.geo.integrated.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.geo.integrated.model.SysUser;
import com.geo.integrated.service.SysUserService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author: whtli
 * @date: 2023/01/16
 * @description: token工具类
 */
@Component
public class TokenUtils {
    private static SysUserService staticSysUserService;

    @Resource
    private SysUserService sysUserService;

    @PostConstruct
    public void setUserService() {
        staticSysUserService = sysUserService;
    }

    /**
     * 生成token
     *
     * @return
     */
    public static String generateToken(Long userId, String sign) {
        // 将 user id 保存到 token 里面,作为载荷
        // 2小时后token过期
        // 以 password 作为 token 的密钥
        return JWT.create().withAudience(String.valueOf(userId))
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2))
                .sign(Algorithm.HMAC256(sign));
    }

    /**
     * 获取当前登录的用户信息
     *
     * @return user对象
     */
    public static SysUser getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("Authorization");
            if (StrUtil.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                return staticSysUserService.getById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
