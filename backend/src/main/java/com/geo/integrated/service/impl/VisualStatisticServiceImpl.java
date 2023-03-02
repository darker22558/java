package com.geo.integrated.service.impl;

import cn.hutool.core.date.DateUtil;
import com.geo.integrated.dao.VisualStatisticMapper;
import com.geo.integrated.entity.AchievementProject;
import com.geo.integrated.model.vo.ProjectStatistic;
import com.geo.integrated.service.VisualStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: whtli
 * @date: 2023/02/09
 * @description: 网站数据统计业务层实现
 */
@Slf4j
@Service
public class VisualStatisticServiceImpl implements VisualStatisticService {

    @Resource
    private VisualStatisticMapper visualStatisticMapper;

    /**
     * 获取项目分类及年份统计数据
     *
     * @return 项目分类及年份统计数据
     */
    @Override
    public Map<String, Object> getProjectStatistic() {
        // 查询项目分类数据
        List<ProjectStatistic> projectTypeList = visualStatisticMapper.getProjectTypeList();
        // 使用stream获取分类名列表，与for循环效果相同
        List<String> typeName = projectTypeList.stream().map(ProjectStatistic::getName).collect(Collectors.toList());
        // 获取所有项目列表
        List<AchievementProject> projectList = visualStatisticMapper.getProjectList();
        // 获取项目总数
        int blogCount = projectList.size();

        // 获取各年份项目数据
        Map<Integer, Integer> projectYearCount = new LinkedHashMap<>();
        for (AchievementProject project : projectList) {
            String startDate = project.getStartDate();
            Date date = new Date();
            if (startDate == null) {
                startDate = "1800";
            }
            try {
                date = new SimpleDateFormat("yyyy").parse(startDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            int year = DateUtil.year(date);
            projectYearCount.put(year, projectYearCount.getOrDefault(year, 0) + 1);

        }
        Map<String, Object> map = new HashMap<>(5);
        map.put("projectCount", blogCount);
        map.put("projectTypeList", projectTypeList);
        map.put("typeName", typeName);
        map.put("projectYearCount", projectYearCount);
        return map;
    }

    /**
     * 获取总PV
     *
     * @return 总PV值
     */
    @Override
    public int getTotalPageView() {
        return 0;
    }

    /**
     * 获取当日PV
     *
     * @return 日PV值
     */
    @Override
    public int getTodayPageView() {
        return 0;
    }

    /**
     * 获取总UV
     *
     * @return 总UV值
     */
    @Override
    public int getTotalUniqueVisitor() {
        return 0;
    }

    /**
     * 获取日UV
     *
     * @return 日UV值
     */
    @Override
    public int getTodayUniqueVisitor() {
        return 0;
    }

    /**
     * 获取荣誉总数
     *
     * @return 荣誉总数
     */
    @Override
    public int getHonorCount() {
        return visualStatisticMapper.getHonorCount();
    }

    /**
     * 获取项目总数
     *
     * @return 项目总数
     */
    @Override
    public int getProjectCount() {
        return visualStatisticMapper.getProjectCount();
    }

    /**
     * 获取发表的论文总数
     *
     * @return 发表的论文总数
     */
    @Override
    public int getPaperPublishedCount() {
        return visualStatisticMapper.getPaperPublishedCount();
    }

    /**
     * 获取发明专利总数
     *
     * @return 发明专利总数
     */
    @Override
    public int getPatentCount() {
        return visualStatisticMapper.getPatentCount();
    }
}
