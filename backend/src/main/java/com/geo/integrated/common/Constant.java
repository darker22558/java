package com.geo.integrated.common;

/**
 * @author: whtli
 * @date: 2023/01/14
 * @description: 常量
 */
public interface Constant {
    /**
     * 操作成功
     */
    Integer CODE_SUCCESS = 200;

    /**
     * 对象创建成功
     */
    Integer CODE_CREATED = 201;

    /**
     * 请求已经被接受
     */
    Integer CODE_ACCEPTED = 202;

    /**
     * 操作已经执行成功，但是没有返回数据
     */
    Integer CODE_NO_CONTENT = 204;

    /**
     * 资源已被移除
     */
    Integer CODE_MOVED_PERM = 301;

    /**
     * 重定向
     */
    Integer CODE_SEE_OTHER = 303;

    /**
     * 资源没有被修改
     */
    Integer CODE_NOT_MODIFIED = 304;

    /**
     * 参数列表错误（缺少，格式不匹配）
     */
    Integer CODE_BAD_REQUEST = 400;

    /**
     * 未授权
     */
    Integer CODE_UNAUTHORIZED = 401;

    /**
     * 访问受限，授权过期
     */
    Integer CODE_FORBIDDEN = 403;

    /**
     * 资源，服务未找到
     */
    Integer CODE_NOT_FOUND = 404;

    /**
     * 不允许的http方法
     */
    Integer CODE_BAD_METHOD = 405;

    /**
     * 资源冲突，或者资源被锁
     */
    Integer CODE_CONFLICT = 405;

    /**
     * 不支持的数据，媒体类型
     */
    Integer CODE_UNSUPPORTED_TYPE = 415;

    /**
     * 系统内部错误
     */
    Integer CODE_SYSTEM_ERROR = 500;

    /**
     * 接口未实现
     */
    Integer CODE_NOT_IMPLEMENTED = 501;

    /**
     * 其他业务异常
     */
    Integer CODE_ELSE_ERROR = 600;

    /**
     * 系统警告消息
     */
    Integer CODE_WARN = 601;

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

    /**
     * 定时任务测试常量
     */
    String TASK_STRING = "定时任务测试常量";
}
