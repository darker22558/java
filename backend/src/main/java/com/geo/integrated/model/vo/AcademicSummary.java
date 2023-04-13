package com.geo.integrated.model.vo;

import com.geo.integrated.entity.AchievementProject;
import com.geo.integrated.model.dto.ProjectStatistic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/04/13
 * @description: 学术成果信息统计VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcademicSummary {
    /**
     * 项目分类数据
     */
    private List<ProjectStatistic> projectTypeList;
    /**
     * 项目分类名列表
     */
    private List<String> typeName;
    /**
     * 项目列表
     */
    private List<AchievementProject> projectList;
    /**
     * 各年份项目数据
     */
    private Map<Integer, Integer> projectYearCount;
    /**
     * 项目总数
     */
    private int totalProject;
    /**
     * 荣誉总数
     */
    private int totalHonor;
    /**
     * 发表论文总数
     */
    private int totalPaperPublished;
    /**
     * 专利总数
     */
    private int totalPatent;
}
