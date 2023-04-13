package com.geo.integrated.service.impl;

import com.geo.integrated.dao.VisualStatisticMapper;
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
}
