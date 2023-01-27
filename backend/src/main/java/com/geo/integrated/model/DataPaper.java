package com.geo.integrated.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author: whtli
 * @date: 2023/01/19
 * @description: 文献信息实体类
 */
@Data
@TableName("data_paper")
public class DataPaper implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * eid
     */
    private String eid;

    /**
     * 论文标题
     */
    private String title;

    /**
     * 发表时间
     */
    private Date publicDate;

    /**
     * 标准国际刊号
     */
    private String issn;

    /**
     * 备注
     */
    private String remark;

    private static final long serialVersionUID = 1L;
}