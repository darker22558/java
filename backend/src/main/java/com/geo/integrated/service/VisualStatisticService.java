package com.geo.integrated.service;

import com.geo.integrated.model.vo.VisitorInfoSummary;

import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/02/09
 * @description: 网站数据统计业务层
 */
public interface VisualStatisticService {
    /**
     * 网站访客信息数据统计
     *
     * @return
     */
    VisitorInfoSummary getStatisticalData();
}
