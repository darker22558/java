package com.geo.integrated.service.impl;

import cn.hutool.core.date.DateUtil;
import com.geo.integrated.dao.AchievementHonorMapper;
import com.geo.integrated.dao.AchievementPatentMapper;
import com.geo.integrated.dao.VisualAcademicMapper;
import com.geo.integrated.entity.AchievementHonor;
import com.geo.integrated.entity.AchievementPatent;
import com.geo.integrated.entity.AchievementProject;
import com.geo.integrated.model.dto.ProjectStatistic;
import com.geo.integrated.model.vo.AcademicSummary;
import com.geo.integrated.service.VisualAcademicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: whtli
 * @date: 2023/04/13
 * @description: 学术成果信息统计业务层实现
 */
@Service
public class VisualAcademicServiceImpl implements VisualAcademicService {

    @Resource
    private VisualAcademicMapper visualAcademicMapper;

    @Resource
    private AchievementPatentMapper achievementPatentMapper;

    @Resource
    private AchievementHonorMapper achievementHonorMapper;


    @Override
    public AcademicSummary getAcademicSummaryDate() {
        // 查询项目分类数据
        List<ProjectStatistic> projectTypeList = visualAcademicMapper.getProjectTypeList();
        // 使用stream获取分类名列表，与for循环效果相同
        List<String> typeName = projectTypeList.stream().map(ProjectStatistic::getName).collect(Collectors.toList());
        // 获取项目列表
        List<AchievementProject> projectList = visualAcademicMapper.getProjectList();

        // 获取各年份项目数据
        Map<Integer, Integer> projectYearCount = new LinkedHashMap<>();
        for (AchievementProject project : projectList) {
            String startDate = project.getStartDate();
            Date date = new Date();
            if (startDate == null) {
                startDate = "2000";
            }
            try {
                date = new SimpleDateFormat("yyyy").parse(startDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            int year = DateUtil.year(date);
            projectYearCount.put(year, projectYearCount.getOrDefault(year, 0) + 1);
        }
        int totalProject = projectList.size();
        int totalHonor = getHonorCount();
        int totalPaperPublished = getPaperPublishedCount();
        int totalPatent = getPatentCount();

        AcademicSummary academicSummary = new AcademicSummary(
                projectTypeList, typeName, projectList, projectYearCount,
                totalProject, totalHonor, totalPaperPublished, totalPatent);

        return academicSummary;
    }


    public int getHonorCount() {
        List<AchievementHonor> honorList = achievementHonorMapper.selectList(null);
        return honorList == null ? 0 : honorList.size();
    }

    public int getPaperPublishedCount() {
        return 0;
    }

    public int getPatentCount() {
        List<AchievementPatent> patentList = achievementPatentMapper.selectList(null);
        return patentList == null ? 0 : patentList.size();
    }

}
