package com.geo.integrated.model.dto;

import com.geo.integrated.entity.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author: whtli
 * @date: 2023/02/08
 * @description: SpringSecurity需要的用户详情
 */
public class SysUserDetails implements UserDetails {
    private SysUser sysUser;
    // private List<SysUserPermission> permissionList;

    /**
     * 构造方法
     * @param sysUser 用户实体类
     */
    public SysUserDetails(SysUser sysUser) {
        this.sysUser = sysUser;
        // this.permissionList = permissionList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
        // 返回当前用户的权限
        /* return permissionList.stream()
                .filter(permission -> permission.getValue()!=null)
                .map(permission ->new SimpleGrantedAuthority(permission.getValue()))
                .collect(Collectors.toList());*/
    }

    @Override
    public String getPassword() {
        return sysUser.getPassword();
    }

    @Override
    public String getUsername() {
        return sysUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
