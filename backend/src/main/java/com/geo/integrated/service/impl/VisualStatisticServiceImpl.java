package com.geo.integrated.service.impl;

import com.geo.integrated.dao.VisualStatisticMapper;
import com.geo.integrated.model.vo.VisitorInfoSummary;
import com.geo.integrated.service.VisualStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    @Override
    public VisitorInfoSummary getStatisticalData() {
        // TODO: 访问信息入库、使用redis获取日PV和日UV
        // 总PV
        int totalPageView = getTotalPageView();
        // 日PV
        int todayPageView = getTodayPageView();
        // 总UV
        int totalUniqueVisitor = getTotalUniqueVisitor();
        // 日UV
        int todayUniqueVisitor = getTodayUniqueVisitor();
        VisitorInfoSummary visitorInfoSummary = new VisitorInfoSummary(totalPageView, todayPageView, totalUniqueVisitor, todayUniqueVisitor);
        return visitorInfoSummary;
    }

    /**
     * 获取总PV
     *
     * @return 总PV值
     */
    public int getTotalPageView() {
        int totalPageView = visualStatisticMapper.getTotalPageView();
        return totalPageView;
    }

    /**
     * 获取当日PV
     *
     * @return 日PV值
     */
    public int getTodayPageView() {
        int todayPageView = visualStatisticMapper.getTodayPageView();
        return todayPageView;
    }

    /**
     * 获取总UV
     *
     * @return 总UV值
     */
    public int getTotalUniqueVisitor() {
        int totalUniqueVisitor = visualStatisticMapper.getTotalUniqueVisitor();
        return totalUniqueVisitor;
    }

    /**
     * 获取日UV
     *
     * @return 日UV值
     */
    public int getTodayUniqueVisitor() {
        int todayUniqueVisitor = visualStatisticMapper.getTodayUniqueVisitor();
        return todayUniqueVisitor;
    }
}
