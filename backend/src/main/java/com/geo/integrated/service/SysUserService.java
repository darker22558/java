package com.geo.integrated.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.geo.integrated.model.dto.LoginDTO;
import com.geo.integrated.model.SysUser;

/**
 * @author: whtli
 * @date: 2023/01/16
 * @description:
 */
public interface SysUserService extends IService<SysUser> {
    /**
     * 登录
     *
     * @param loginDTO
     * @return
     */
    SysUser login(LoginDTO loginDTO);
}
