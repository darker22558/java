package com.geo.integrated.service;

import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/02/09
 * @description: 网站数据统计业务层
 */
public interface VisualStatisticService {
    /**
     * 获取项目分类及年份统计数据
     *
     * @return 项目分类及年份统计数据
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

    /**
     * 获取荣誉总数
     *
     * @return 荣誉总数
     */
    int getHonorCount();

    /**
     * 获取项目总数
     *
     * @return 项目总数
     */
    int getProjectCount();

    /**
     * 获取发表的论文总数
     *
     * @return 发表的论文总数
     */
    int getPaperPublishedCount();

    /**
     * 获取发明专利总数
     *
     * @return 发明专利总数
     */
    int getPatentCount();
}
