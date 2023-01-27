package com.geo.integrated.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author: whtli
 * @date: 2023/01/26
 * @description: 煤田信息实体类
 */
@Data
@TableName("data_coalfield")
public class DataCoalfield implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 煤田名称
     */
    private String coalfieldName;

    /**
     * 所属省份
     */
    private String province;

    /**
     * 所属聚煤区
     */
    private String coalCoveringArea;

    /**
     * 成煤期
     */
    private String coalFormingPeriods;

    /**
     * 样本数量
     */
    private Integer sampleNumber;

    /**
     * 灰分产量（ %; Dry Basis）
     */
    private String ashYield;

    /**
     * 相关论文
     */
    private String relevantPaper;

    /**
     * 备注
     */
    private String remark;

    private static final long serialVersionUID = 1L;
}