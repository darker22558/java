package com.geo.integrated.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geo.integrated.dao.LogExceptionMapper;
import com.geo.integrated.entity.LogException;
import com.geo.integrated.service.LogExceptionService;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author: whtli
 * @date: 2023/01/28
 * @description: 异常日志业务层实现
 */
@Service
public class LogExceptionServiceImpl extends ServiceImpl<LogExceptionMapper, LogException> implements LogExceptionService {
    @Resource
    private LogExceptionMapper logExceptionMapper;

    /**
     * 报错异常日志
     *
     * @param log 异常日志对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveExceptionLog(LogException log) {
        int count = logExceptionMapper.insert(log);
        if (count != 1) {
            throw new PersistenceException("日志添加失败");
        }
    }
}
