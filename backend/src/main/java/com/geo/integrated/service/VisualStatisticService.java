package com.geo.integrated.service;

import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/02/09
 * @description:
 */
public interface VisualStatisticService {
    /**
     * 调用Mapper层获取统计数据
     *
     * @return 存放了项目统计数据的Map
     */
    Map<String, Object> getProjectStatistic();

    /**
     * 获取总PV
     *
     * @return 总PV值
     */
    int getTotalPageView();

    /**
     * 获取当日PV
     *
     * @return 日PV值
     */
    int getTodayPageView();

    /**
     * 获取总UV
     *
     * @return 总UV值
     */
    int getTotalUniqueVisitor();

    /**
     * 获取日UV
     *
     * @return 日UV值
     */
    int getTodayUniqueVisitor();

    int getHonorCount();

    int getProjectCount();

    int getPaperPublishedCount();

    int getPatentCount();
}
