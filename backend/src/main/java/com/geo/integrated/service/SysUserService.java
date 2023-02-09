package com.geo.integrated.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.geo.integrated.common.Result;
import com.geo.integrated.model.dto.LoginDTO;
import com.geo.integrated.entity.SysUser;

/**
 * @author: whtli
 * @date: 2023/01/16
 * @description: 用户管理服务层
 */
public interface SysUserService extends IService<SysUser> {
    /**
     * 用户登录匹配
     * 查询是否有与当前表单中的用户名、密码匹配的用户信息
     *
     * @param username 用户名
     * @param password 密码
     * @return 匹配成功返回用户实体类，失败返回null
     */
    SysUser login(String username, String password);

    /**
     * 生成验证码
     *
     * @return 验证码
     */
    Result generateAuthCode();

    /**
     * 对输入的验证码进行校验
     *
     * @param uniqueLoginId 唯一登录标识
     * @param authCode      验证码
     * @return 验证码的校验结果
     */
    boolean verifyAuthCode(String uniqueLoginId, String authCode);

    /**
     * 生成token
     *
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    String generateToken(String username, String password);
}
