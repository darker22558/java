package com.geo.integrated.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geo.integrated.dao.LogExceptionMapper;
import com.geo.integrated.entity.LogException;
import com.geo.integrated.service.LogExceptionService;
import com.geo.integrated.utils.IpAddressUtils;
import com.geo.integrated.utils.UserAgentUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/01/28
 * @description:
 */
@Service
public class LogExceptionServiceImpl extends ServiceImpl<LogExceptionMapper, LogException> implements LogExceptionService {
    @Autowired
    private LogExceptionMapper logExceptionMapper;
    @Autowired
    private UserAgentUtils userAgentUtils;
    @Transactional
    @Override
    public void saveExceptionLog(LogException log) {
        int count = logExceptionMapper.insert(log);
        if (count != 1) {
            throw new PersistenceException("日志添加失败");
        }
    }
}
