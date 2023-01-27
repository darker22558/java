package com.geo.integrated.common;

/**
 * @author: whtli
 * @date: 2023/01/14
 * @description: 常量
 */
public interface Constant {
    /**
     * 成功
     */
    Integer CODE_SUCCESSFUL = 200;

    /**
     * 参数错误
     */
    Integer CODE_PARAM_ERROR = 400;

    /**
     * 权限不足
     */
    Integer CODE_ACCESS_DENIED = 401;

    /**
     * 系统错误
     */
    Integer CODE_SYSTEM_ERROR = 500;

    /**
     * 其他业务异常
     */
    Integer CODE_ELSE_ERROR = 600;

    /**
     * 未知IP
     */
    String IP_UNKNOWN = "unknown";

    /**
     * ipv4本机地址
     */
    String IP_V4_LOCALHOST = "127.0.0.1";

    /**
     * ipv6本机地址
     */
    String IP_V6_LOCALHOST = "0:0:0:0:0:0:0:1";
}
