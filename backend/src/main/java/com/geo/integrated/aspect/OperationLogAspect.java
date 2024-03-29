package com.geo.integrated.aspect;

import com.geo.integrated.annotation.OperationLogger;
import com.geo.integrated.entity.LogOperation;
import com.geo.integrated.service.LogOperationService;
import com.geo.integrated.utils.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/01/27
 * @description: 记录操作日志的切面
 */
@Component
@Aspect
public class OperationLogAspect {
    @Resource
    private LogOperationService logOperationService;

    @Resource
    private UserAgentUtils userAgentUtils;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    ThreadLocal<Long> currentTime = new ThreadLocal<>();

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(operationLogger)")
    public void logPointcut(OperationLogger operationLogger) {
    }

    /**
     * 配置环绕通知
     *
     * @param joinPoint       连接点
     * @param operationLogger 注解OperationLogger对象
     * @return joinPoint.proceed()：核心方法的执行结果
     * @throws Throwable
     */
    @Around("logPointcut(operationLogger)")
    public Object logAround(ProceedingJoinPoint joinPoint, OperationLogger operationLogger) throws Throwable {
        currentTime.set(System.currentTimeMillis());
        Object result = joinPoint.proceed();
        int times = (int) (System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        // 新建一个当前操作的日志对象并填充
        LogOperation operationLog = handleLog(joinPoint, operationLogger, times);
        // 将操作日志保持到数据库
        logOperationService.save(operationLog);
        return result;
    }

    /**
     * 获取HttpServletRequest请求对象，并设置LogOperation对象属性
     *
     * @param joinPoint       切入点
     * @param operationLogger 注解OperationLogger对象
     * @param times           操作所用时间
     * @return 操作日志实体类
     */
    private LogOperation handleLog(ProceedingJoinPoint joinPoint, OperationLogger operationLogger, int times) {
        // 获取操作描述
        String description = operationLogger.value();
        // 获取请求内容中的属性
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 获取请求属性
        HttpServletRequest request = attributes.getRequest();
        // 获取authHeader
        String authHeader = request.getHeader(this.tokenHeader);
        // tokenHead之后的部分是token
        String authToken = authHeader.substring(this.tokenHead.length());
        // 根据token获取用户名
        String username = jwtUtils.getUserNameFromToken(authToken);
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
        // 创建日志对象
        LogOperation log = new LogOperation(username, description, uri, method, userAgent, ip, ipSource, times, param, os, browser);
        return log;
    }
}
