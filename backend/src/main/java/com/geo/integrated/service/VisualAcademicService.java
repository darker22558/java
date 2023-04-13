package com.geo.integrated.service;

import com.geo.integrated.model.vo.AcademicSummary;

import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/04/13
 * @description: 学术成果统计信息业务层
 */
public interface VisualAcademicService {
    /**
     * 获取学术成果统计信息
     *
     * @return 学术成果信息统计结果
     */
    AcademicSummary getAcademicSummaryDate();
}
