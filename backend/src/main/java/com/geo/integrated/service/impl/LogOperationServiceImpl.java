package com.geo.integrated.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geo.integrated.dao.LogOperationMapper;
import com.geo.integrated.model.LogOperation;
import com.geo.integrated.service.LogOperationService;
import org.springframework.stereotype.Service;

/**
 * @author: whtli
 * @date: 2023/01/27
 * @description:
 */
@Service
public class LogOperationServiceImpl extends ServiceImpl<LogOperationMapper, LogOperation> implements LogOperationService {
}
