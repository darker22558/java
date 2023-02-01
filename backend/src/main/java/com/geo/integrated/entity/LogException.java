package com.geo.integrated.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * log_exception
 *
 * @author
 */
@Data
@TableName("log_exception")
public class LogException implements Serializable {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
     * 操作描述
     */
    @ApiModelProperty(value = "操作描述")
    private String description;

    /**
     * 异常信息
     */
    @ApiModelProperty(value = "异常信息")
    private String error;

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
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private Date createTime;

    /**
     * user-agent用户代理
     */
    @ApiModelProperty(value = "用户代理")
    private String userAgent;

    private static final long serialVersionUID = 1L;

    public LogException(String uri, String method, String param, String description, String error, String ip, String ipSource, String os, String browser, String userAgent) {
        this.uri = uri;
        this.method = method;
        this.param = param;
        this.description = description;
        this.error = error;
        this.ip = ip;
        this.ipSource = ipSource;
        this.os = os;
        this.browser = browser;
        this.createTime = new Date();
        this.userAgent = userAgent;
    }
}