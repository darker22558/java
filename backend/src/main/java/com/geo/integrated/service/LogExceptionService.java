package com.geo.integrated.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.geo.integrated.entity.LogException;
import org.springframework.scheduling.annotation.Async;

/**
 * @author: whtli
 * @date: 2023/01/28
 * @description: 异常日志业务层
 */
public interface LogExceptionService extends IService<LogException> {
    /**
     * 报错异常日志
     *
     * @param log 异常日志对象
     */
    @Async
    void saveExceptionLog(LogException log);
}
