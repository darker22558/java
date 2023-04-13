package com.geo.integrated.dao;

import com.geo.integrated.entity.AchievementProject;
import com.geo.integrated.model.dto.ProjectStatistic;

import java.util.List;

/**
 * @author: whtli
 * @date: 2023/04/13
 * @description: 学术成果数据统计持久层
 */
public interface VisualAcademicMapper {
    /**
     * 获取项目分类列表
     *
     * @return 项目分类及各分类下项目数量
     */
    List<ProjectStatistic> getProjectTypeList();

    /**
     * 获取项目列表
     *
     * @return 项目列表
     */
    List<AchievementProject> getProjectList();

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
