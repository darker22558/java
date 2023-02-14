package com.geo.integrated.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geo.integrated.common.Result;
import com.geo.integrated.dao.SysUserMapper;
import com.geo.integrated.entity.SysUser;
import com.geo.integrated.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author: whtli
 * @date: 2023/01/16
 * @description: 用户相关功能服务层实现
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Result saveUserInfo(SysUser user) {
        // 查询是否存在同名用户信息
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        SysUser example = sysUserMapper.selectOne(queryWrapper);
        if (example != null) {
            return Result.fail("已经存在用户名为" + user.getUsername() + "的用户，请重新填写用户名");
        } else {
            // 新用户和创建时间和更新时间相同
            Date date = new Date();
            user.setUpdateTime(date);
            user.setCreateTime(date);
            // 对密码做加密
            String encodePassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodePassword);
            // 用户信息保存到数据库
            int flag = sysUserMapper.insert(user);
            if (flag == 1) {
                return Result.success("新增用户成功");
            } else {
                return Result.fail("新增用户失败");
            }
        }
    }

    @Override
    public Result updateUserInfo(SysUser user) {
        // 从数据库中获取修改前的用户信息
        SysUser originUserInfo = sysUserMapper.selectById(user.getId());
        // 验证密码的修改情况，如果传入的密码与数据库中密码的明文不同
        if (!user.getPassword().equals(originUserInfo.getPassword())) {
            // 将传入的密码加密后，再与数据库中的密码对比
            if (!passwordEncoder.matches(user.getPassword(), originUserInfo.getPassword())) {
                // 加密后不同则是修改了密码，数据库中需要修改
                log.info("管理系统中修改了用户 === {} 的密码，加密后不同，数据库中需要修改", user.getUsername());
                String encodePassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodePassword);
            } else {
                log.info("管理系统中修改了用户 === {} 的密码，加密后相同，数据库中不需要修改", user.getUsername());
                // 加密后相同，则数据库中不需要修改
                user.setPassword(originUserInfo.getPassword());
            }
        } else {
            log.info("管理系统中未修改用户 === {} 的密码", user.getUsername());
        }
        // 更新用户信息的`更新时间`
        Date date = new Date();
        user.setUpdateTime(date);
        // 用户信息保存到数据库
        int flag = sysUserMapper.updateById(user);
        if (flag == 1) {
            return Result.success("用户信息更新成功");
        } else {
            return Result.fail("用户信息更新失败");
        }
    }
}
