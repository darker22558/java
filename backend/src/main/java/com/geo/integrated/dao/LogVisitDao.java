package com.geo.integrated.dao;

import com.geo.integrated.model.LogVisit;

public interface LogVisitDao {
    int deleteByPrimaryKey(Long id);

    int insert(LogVisit record);

    int insertSelective(LogVisit record);

    LogVisit selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LogVisit record);

    int updateByPrimaryKey(LogVisit record);
}