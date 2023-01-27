package com.geo.integrated.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geo.integrated.dao.SysUserMapper;
import com.geo.integrated.model.dto.LoginDTO;
import com.geo.integrated.model.SysUser;
import com.geo.integrated.service.SysUserService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @author: whtli
 * @date: 2023/01/16
 * @description: 用户相关功能业务实现层
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

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
}
