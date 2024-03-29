package com.geo.integrated.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/02/08
 * @description: 用于生成和解析JWT token的工具类
 */
@Component
@Slf4j
public class JwtUtils {
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final Integer TIME_SECOND = 60;
    private static final Integer TIME_RANGE = 30;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * 根据用户信息填充负载，调用生成token的方法
     */
    public String generateTokenByUserDetails(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 根据负载生成JWT
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从token中获取负载（校验Token）
     */
    private Claims getClaimsFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        } catch (Exception e) {
            log.info("JWT验证失败 === {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从token中获取登录用户名
     */
    public String getUserNameFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            log.info("获取登录用户名失败 === {}", e.getMessage());
            return null;
        }
    }

    /**
     * 验证token是否有效
     *
     * @param token       客户端传入的token
     * @param userDetails 从数据库中查询出来的用户信息
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUserNameFromToken(token);
        if (username == null) {
            return false;
        } else {
            // 如果token中解析出的用户名与数据库中查询出的用户名相同，且token未过期，则token有效
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        }
    }

    /**
     * 判断token是否已经过期失效
     */
    private boolean isTokenExpired(String token) {
        // 从token中获取过期时间
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return false;
        } else {
            Date expiredDate = claims.getExpiration();
            return expiredDate.before(new Date());
        }
    }

    /**
     * 判断token是否到达需要刷新的时间范围
     */
    public boolean isTokenNeedToBeRefreshed(String token) {
        // 从token中获取过期时间
        Claims claims = getClaimsFromToken(token);
        Date created = claims.get(CLAIM_KEY_CREATED, Date.class);
        Date now = new Date();
        Date exp = claims.getExpiration();
        // 当前时间在失效时间之前的30分钟内
        log.info("创建时间 === {}", created);
        log.info("当前时间 === {}", now);
        log.info("过期时间 === {}", exp);
        log.info("刷新范围 === {}", DateUtil.offsetSecond(exp, - TIME_RANGE * TIME_SECOND));
        // 判断当前时间是否在[过期时间前30分钟，过期时间]的区间范围内
        if (now.after(DateUtil.offsetSecond(exp, - TIME_RANGE * TIME_SECOND)) && now.before(exp)) {
            log.info("需要刷新 === token");
            return true;
        }
        return false;
    }

    /**
     * 当原来的token没过期时是可以刷新的
     *
     * @param oldToken 带tokenHead的token
     */
    public String refreshHeadToken(String oldToken) {
        // 空串
        if (StrUtil.isEmpty(oldToken)) {
            return null;
        }
        // 获取token
        String token = oldToken.substring(tokenHead.length());
        // token是空
        if (StrUtil.isEmpty(token)) {
            return null;
        }
        // token校验不通过
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return null;
        }
        // 如果token已经过期，不支持刷新
        if (isTokenExpired(token)) {
            return null;
        }
        // 如果token在30分钟之内刚刷新过，返回原token
        if (tokenRefreshJustBefore(token, TIME_RANGE * TIME_SECOND)) {
            return token;
        } else {
            claims.put(CLAIM_KEY_CREATED, new Date());
            return generateToken(claims);
        }
    }

    /**
     * 判断token在指定时间内是否刚刚刷新过
     *
     * @param token 原token
     * @param time  指定时间（秒）
     */
    private boolean tokenRefreshJustBefore(String token, int time) {
        Claims claims = getClaimsFromToken(token);
        Date created = claims.get(CLAIM_KEY_CREATED, Date.class);
        Date refreshDate = new Date();
        // 当前时间在token创建后的30分钟之内
        if (refreshDate.after(created) && refreshDate.before(DateUtil.offsetSecond(created, time))) {
            return true;
        } else {
            return false;
        }
    }
}
