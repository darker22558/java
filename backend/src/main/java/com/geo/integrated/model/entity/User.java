package com.geo.integrated.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: whtli
 * @date: 2023/01/16
 * @description: 用户实体类
 */
@Data
@Accessors(chain = true)
@TableName("sys_user")
public class User implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "昵称不能为空")
    private String username;

    /**
     * @JsonIgnore
     */
    private String password;

    private String nickname;

    private String avatar;
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    private Date createTime;

    private Date updateTime;

    private String role;

    private static final long serialVersionUID = 1L;

}
