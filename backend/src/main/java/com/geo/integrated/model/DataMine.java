package com.geo.integrated.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * data_mine
 * @author 
 */
@Data
@TableName("data_mine")
public class DataMine implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 煤矿名
     */
    private String mineName;

    /**
     * 煤田名
     */
    private String coalfieldName;

    /**
     * 论文标题
     */
    private String paperName;

    /**
     * 论文链接
     */
    private String paperUrl;

    /**
     * 备注
     */
    private String remark;

    private static final long serialVersionUID = 1L;
}