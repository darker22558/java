package com.geo.integrated.dao;

import com.geo.integrated.model.LogOperation;

public interface LogOperationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(LogOperation record);

    int insertSelective(LogOperation record);

    LogOperation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LogOperation record);

    int updateByPrimaryKey(LogOperation record);
}