package com.geo.integrated.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 煤田名称
     */
    @ApiModelProperty(value = "煤田名称")
    private String coalfieldName;

    /**
     * 所属省份
     */
    @ApiModelProperty(value = "所属省份")
    private String province;

    /**
     * 所属聚煤区
     */
    @ApiModelProperty(value = "所属聚煤区")
    private String coalCoveringArea;

    /**
     * 成煤期
     */
    @ApiModelProperty(value = "成煤期")
    private String coalFormingPeriods;

    /**
     * 样本数量
     */
    @ApiModelProperty(value = "灰分产量")
    private Integer sampleNumber;

    /**
     * 灰分产量（ %; Dry Basis）
     */
    @ApiModelProperty(value = "灰分产量")
    private String ashYield;

    /**
     * 相关论文
     */
    @ApiModelProperty(value = "相关论文")
    private String relevantPaper;

    /**
     * 论文链接
     */
    @ApiModelProperty(value = "论文链接")
    private String link;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    private static final long serialVersionUID = 1L;
}