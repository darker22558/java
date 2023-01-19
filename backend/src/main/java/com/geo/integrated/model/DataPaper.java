package com.geo.integrated.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * data_paper
 * @author 
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
    private String paperTitle;

    /**
     * 发表时间
     */
    private Date publicData;

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