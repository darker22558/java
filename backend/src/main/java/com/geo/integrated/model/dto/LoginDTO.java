package com.geo.integrated.model.dto;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String authCode;

    @ApiModelProperty(value = "唯一登录标识")
    @NotBlank(message = "登录标识不能为空")
    private String uniqueLoginId;


}
