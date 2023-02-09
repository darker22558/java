package com.geo.integrated.dao;

import com.geo.integrated.entity.AchievementProject;
import com.geo.integrated.model.vo.ProjectStatistic;

import java.util.List;

/**
 * @author: whtli
 * @date: 2023/02/09
 * @description:
 */
public interface VisualStatisticMapper {
    List<ProjectStatistic> getProjectTypeList();

    List<AchievementProject> getProjectList();
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
