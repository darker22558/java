package com.geo.integrated.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author: whtli
 * @date: 2023/01/16
 * @description: 登录的DTO
 */
@Data
public class LoginDTO implements Serializable {
    @NotBlank(message = "昵称不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
