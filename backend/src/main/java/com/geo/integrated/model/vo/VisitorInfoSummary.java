package com.geo.integrated.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: whtli
 * @date: 2023/04/13
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitorInfoSummary {
    /**
     * 总PV
     */
    private int totalPageView;
    /**
     * 日PV
     */
    private int todayPageView;
    /**
     * 总UV
     */
    private int totalUniqueVisitor;
    /**
     * 日UV
     */
    private int todayUniqueVisitor;
}
