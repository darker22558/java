package com.geo.integrated.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geo.integrated.entity.LogException;

public interface LogExceptionMapper extends BaseMapper<LogException> {
    int deleteByPrimaryKey(Long id);

    int insert(LogException record);

    int insertSelective(LogException record);

    LogException selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LogException record);

    int updateByPrimaryKey(LogException record);
}