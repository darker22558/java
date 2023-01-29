package com.geo.integrated.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.geo.integrated.entity.LogException;
import org.springframework.scheduling.annotation.Async;

/**
 * @author: whtli
 * @date: 2023/01/28
 * @description:
 */
public interface LogExceptionService extends IService<LogException> {
    @Async
    void saveExceptionLog(LogException log);
}
