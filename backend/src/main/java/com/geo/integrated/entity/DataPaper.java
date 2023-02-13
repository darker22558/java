package com.geo.integrated.entity;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: whtli
 * @date: 2023/01/19
 * @description: 文献信息实体类
 */
@Data
@EqualsAndHashCode
@TableName("data_paper")
public class DataPaper implements Serializable {
    /**
     * id
     */
    @ExcelProperty("id")
    @ColumnWidth(20)
    @ExcelIgnore
    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * eid
     */
    @ExcelProperty("eid")
    @ColumnWidth(20)
    @ApiModelProperty(value = "eid")
    private String eid;

    /**
     * 论文标题
     */
    @ExcelProperty("论文标题")
    @ColumnWidth(20)
    @ApiModelProperty(value = "论文标题")
    private String title;

    /**
     * 发表时间
     */
    @ExcelProperty("发表时间")
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd")
    @ApiModelProperty(value = "发表时间")
    private Date publicDate;

    /**
     * 标准国际刊号
     */
    @ExcelProperty("标准国际刊号")
    @ColumnWidth(20)
    @ApiModelProperty(value = "标准国际刊号")
    private String issn;

    /**
     * 备注
     */
    @ExcelProperty("备注")
    @ColumnWidth(20)
    @ApiModelProperty(value = "备注")
    private String remark;

    private static final long serialVersionUID = 1L;
}