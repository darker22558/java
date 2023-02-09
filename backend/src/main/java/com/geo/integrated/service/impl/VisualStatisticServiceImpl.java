package com.geo.integrated.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Month;
import com.geo.integrated.dao.VisualStatisticMapper;
import com.geo.integrated.entity.AchievementProject;
import com.geo.integrated.model.vo.ProjectStatistic;
import com.geo.integrated.service.VisualStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: whtli
 * @date: 2023/02/09
 * @description:
 */
@Slf4j
@Service
public class VisualStatisticServiceImpl implements VisualStatisticService {

    @Resource
    private VisualStatisticMapper visualStatisticMapper;

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
        /* int jan = 0;
        int feb = 0;
        int mar = 0;
        int apr = 0;
        int may = 0;
        int june = 0;
        int july = 0;
        int aug = 0;
        int sep = 0;
        int oct = 0;
        int nov = 0;
        int dec = 0;*/
        for (AchievementProject project : projectList) {
            String startDate = project.getStartDate();
            log.info("startDate =========== : {}", startDate);
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
            // 只统计当年里每月的项目数（项目无月份信息）
            /*if (year == DateUtil.year(new Date())) {
                Month month = DateUtil.monthEnum(startDate);
                switch (month) {
                    case JANUARY:
                        jan += 1;
                        break;
                    case FEBRUARY:
                        feb += 1;
                        break;
                    case MARCH:
                        mar += 1;
                        break;
                    case APRIL:
                        apr += 1;
                        break;
                    case MAY:
                        may += 1;
                        break;
                    case JUNE:
                        june += 1;
                        break;
                    case JULY:
                        july += 1;
                        break;
                    case AUGUST:
                        aug += 1;
                        break;
                    case SEPTEMBER:
                        sep += 1;
                        break;
                    case OCTOBER:
                        oct += 1;
                        break;
                    case NOVEMBER:
                        nov += 1;
                        break;
                    case DECEMBER:
                        dec += 1;
                        break;
                    default:
                        break;
                }
            }*/
        }
        Map<String, Object> map = new HashMap<>(5);
        map.put("projectCount", blogCount);
        map.put("projectTypeList", projectTypeList);
        map.put("typeName", typeName);
        map.put("projectYearCount", projectYearCount);
        // map.put("projectMonthList", CollUtil.newArrayList(jan, feb, mar, apr, may, june, july, aug, sep, oct, nov, dec));
        return map;
    }

    @Override
    public int getTotalPageView() {
        return 0;
    }

    @Override
    public int getTodayPageView() {
        return 0;
    }

    @Override
    public int getTotalUniqueVisitor() {
        return 0;
    }

    @Override
    public int getTodayUniqueVisitor() {
        return 0;
    }

    @Override
    public int getHonorCount() {
        return visualStatisticMapper.getHonorCount();
    }

    @Override
    public int getProjectCount() {
        return visualStatisticMapper.getProjectCount();
    }

    @Override
    public int getPaperPublishedCount() {
        return visualStatisticMapper.getPaperPublishedCount();
    }

    @Override
    public int getPatentCount() {
        return visualStatisticMapper.getPatentCount();
    }
}
