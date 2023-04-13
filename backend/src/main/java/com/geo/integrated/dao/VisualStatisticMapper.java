package com.geo.integrated.dao;

/**
 * @author: whtli
 * @date: 2023/02/09
 * @description: 网站数据统计持久层
 */
public interface VisualStatisticMapper {

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

}
