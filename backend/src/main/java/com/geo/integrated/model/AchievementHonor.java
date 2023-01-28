package com.geo.integrated.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * achievement_honor
 * @author 
 */
@Data
@TableName("achievement_honor")
public class AchievementHonor implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所获荣誉名称
     */
    private String honorName;

    /**
     * 获奖成果名称
     */
    private String achievementName;

    /**
     * 实验室人员排名
     */
    private String participantsRank;

    private static final long serialVersionUID = 1L;
}