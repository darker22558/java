package com.geo.integrated.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geo.integrated.dao.SysRoleMapper;
import com.geo.integrated.entity.SysRole;
import com.geo.integrated.service.SysRoleService;
import org.springframework.stereotype.Service;

/**
 * @author: whtli
 * @date: 2023/02/14
 * @description: 角色管理业务层实现
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
}
