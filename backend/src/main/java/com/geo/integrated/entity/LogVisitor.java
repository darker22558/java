package com.geo.integrated.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: whtli
 * @date: 2023/01/26
 * @description: 访问日志实体类
 */
@Data
@TableName("log_visit")
public class LogVisitor implements Serializable {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 访客标识码
     */
    @ApiModelProperty(value = "访客标识码")
    private String uuid;

    /**
     * 请求接口
     */
    @ApiModelProperty(value = "请求接口")
    private String uri;

    /**
     * 请求方式
     */
    @ApiModelProperty(value = "请求方式")
    private String method;

    /**
     * 请求参数
     */
    @ApiModelProperty(value = "请求参数")
    private String param;

    /**
     * 访问行为
     */
    @ApiModelProperty(value = "访问行为")
    private String behavior;

    /**
     * 访问内容
     */
    @ApiModelProperty(value = "访问内容")
    private String content;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * ip
     */
    @ApiModelProperty(value = "ip")
    private String ip;

    /**
     * ip来源
     */
    @ApiModelProperty(value = "ip来源")
    private String ipSource;

    /**
     * 操作系统
     */
    @ApiModelProperty(value = "操作系统")
    private String os;

    /**
     * 浏览器
     */
    @ApiModelProperty(value = "浏览器")
    private String browser;

    /**
     * 请求耗时（毫秒）
     */
    @ApiModelProperty(value = "请求耗时")
    private Integer times;

    /**
     * 访问时间
     */
    @ApiModelProperty(value = "访问时间")
    private Date createTime;

    /**
     * user-agent用户代理
     */
    @ApiModelProperty(value = "用户代理")
    private String userAgent;

    private static final long serialVersionUID = 1L;
}