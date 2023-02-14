package com.geo.integrated.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * @author: whtli
 * @date: 2023/02/14
 * @description: 角色实体类
 */
@ApiModel(value="com.geo.integrated.entity.SysRole")
@Data
@TableName("sys_role")
public class SysRole implements Serializable {
    /**
     * id
     */
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value="名称")
    private String name;

    /**
     * 描述
     */
    @ApiModelProperty(value="描述")
    private String description;

    /**
     * 唯一标识
     */
    @ApiModelProperty(value="唯一标识")
    private String flag;

    private static final long serialVersionUID = 1L;
}