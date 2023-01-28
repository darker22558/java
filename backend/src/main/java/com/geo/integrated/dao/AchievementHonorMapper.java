package com.geo.integrated.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geo.integrated.model.AchievementHonor;

public interface AchievementHonorMapper extends BaseMapper<AchievementHonor> {
    int deleteByPrimaryKey(Long id);

    int insert(AchievementHonor record);

    int insertSelective(AchievementHonor record);

    AchievementHonor selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AchievementHonor record);

    int updateByPrimaryKey(AchievementHonor record);
}