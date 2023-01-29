package com.geo.integrated.aspect;

import com.geo.integrated.annotation.OperationLogger;
import com.geo.integrated.entity.LogException;
import com.geo.integrated.service.LogExceptionService;
import com.geo.integrated.utils.AopUtils;
import com.geo.integrated.utils.IpAddressUtils;
import com.geo.integrated.utils.JacksonUtils;
import com.geo.integrated.utils.UserAgentUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/01/28
 * @description: 记录异常日志的切面
 */
@Component
@Aspect
public class ExceptionLogAspect {
    @Autowired
    private LogExceptionService logExceptionService;

    @Autowired
    private UserAgentUtils userAgentUtils;

    /**
     * 配置切入点
     */
    @Pointcut("execution(* com.geo.integrated.controller..*.*(..))")
    public void logPointcut() {
    }

    @AfterThrowing(value = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Exception e) {
        LogException log = handleLog(joinPoint, e);
        logExceptionService.saveExceptionLog(log);
    }

    /**
     * 获取HttpServletRequest请求对象，并设置LogException对象属性
     *
     * @param joinPoint 切入点
     * @param e         异常
     * @return
     */
    private LogException handleLog(JoinPoint joinPoint, Exception e) {
        // 从ServletRequestAttributes中获取request信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 获取请求接口
        String uri = request.getRequestURI();
        // 获取请求方式
        String method = request.getMethod();
        // 借助IpAddressUtils工具类获取用户ip
        String ip = IpAddressUtils.getIpAddress(request);
        // 借助IpAddressUtils工具获取ip来源
        String ipSource = IpAddressUtils.getCityInfo(ip);
        // 借助AopUtils工具类获取请求参数
        Map<String, Object> requestParams = AopUtils.getRequestParams(joinPoint);
        String param = JacksonUtils.writeValueAsString(requestParams);
        // 获取用户代理方式
        String userAgent = request.getHeader("User-Agent");
        // 借助UserAgentUtils工具类获取操作系统和浏览器信息
        Map<String, String> userAgentMap = userAgentUtils.parseOsAndBrowser(userAgent);
        String os = userAgentMap.get("os");
        String browser = userAgentMap.get("browser");
        //todo 使用swagger后，可以直接使用注解上的内容作为 LogException 的 description
        String description = getDescriptionFromAnnotations(joinPoint);
        String error = getStackTrace(e);
        LogException log = new LogException(uri, method, param, description, error, ip, ipSource, os, browser, userAgent);
        return log;
    }

    /**
     * 从注解中获取描述
     *
     * @param joinPoint 切入点
     * @return 描述信息
     */
    private String getDescriptionFromAnnotations(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        OperationLogger operationLogger = method.getAnnotation(OperationLogger.class);
        if (operationLogger != null) {
            return operationLogger.value();
        }
        return "";
    }

    /**
     * 获取堆栈信息
     *
     * @param throwable 异常
     * @return 堆栈信息
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }
}
