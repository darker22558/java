package com.geo.integrated.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geo.integrated.entity.AchievementHonor;

/**
 * @author: whtli
 * @date: 2023/01/28
 * @description: 所获荣誉信息数据持久层
 */
public interface AchievementHonorMapper extends BaseMapper<AchievementHonor> {
    int deleteByPrimaryKey(Long id);

    int insert(AchievementHonor record);

    int insertSelective(AchievementHonor record);

    AchievementHonor selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AchievementHonor record);

    int updateByPrimaryKey(AchievementHonor record);
}