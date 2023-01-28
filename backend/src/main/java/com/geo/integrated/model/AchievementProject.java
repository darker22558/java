package com.geo.integrated.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author: whtli
 * @date: 2023/01/28
 * @description: 科研项目信息管理控制层
 */
@Data
@TableName("achievement_project")
public class AchievementProject implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 编号
     */
    private String number;

    /**
     * 项目名称
     */
    private String title;

    /**
     * 负责人
     */
    private String chargePerson;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 终止日期
     */
    private String endDate;

    /**
     * 类别
     */
    private String type;

    /**
     * 经费
     */
    private String funds;

    /**
     * 经费来源
     */
    private String fundsSource;

    private static final long serialVersionUID = 1L;
}