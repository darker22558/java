package com.geo.integrated.model.dto;

import lombok.Data;

/**
 * @author: whtli
 * @date: 2023/02/09
 * @description: 项目统计数据的VO，字段包括分类id、分类名、分类下的项目数量
 */
@Data
public class ProjectStatistic {

    /**
     * 分类id
     */
    private Long typeId;

    /**
     * 分类名
     */
    private String name;

    /**
     * 分类下项目数量
     */
    private Integer value;
}
