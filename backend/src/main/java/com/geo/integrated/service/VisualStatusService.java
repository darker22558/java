package com.geo.integrated.service;

import com.geo.integrated.common.Result;
import com.geo.integrated.model.vo.SystemStatus;

import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/04/13
 * @description: 系统状态信息业务层
 */
public interface VisualStatusService {
    /**
     * 获取系统状态信息
     *
     * @return 系统状态信息
     */
    public SystemStatus getSystemState();
}
