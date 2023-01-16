package com.geo.integrated.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.geo.integrated.model.dto.LoginDTO;
import com.geo.integrated.model.entity.User;

/**
 * @author: whtli
 * @date: 2023/01/16
 * @description:
 */
public interface UserService extends IService<User> {
    /**
     * 登录
     *
     * @param loginDTO
     * @return
     */
    User login(LoginDTO loginDTO);
}
