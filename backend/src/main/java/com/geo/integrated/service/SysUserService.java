package com.geo.integrated.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.geo.integrated.common.Result;
import com.geo.integrated.entity.SysUser;

import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/01/16
 * @description: 用户管理业务层
 */
public interface SysUserService extends IService<SysUser> {
    /**
     * 保存用户信息
     * @param user
     * @return
     */
    Result saveUserInfo(SysUser user);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    Result updateUserInfo(SysUser user);
}
