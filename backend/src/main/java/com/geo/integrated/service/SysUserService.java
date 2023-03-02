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

    Result saveUserInfo(SysUser user);

    Result updateUserInfo(SysUser user);
}
