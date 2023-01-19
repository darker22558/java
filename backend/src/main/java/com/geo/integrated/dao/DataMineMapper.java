package com.geo.integrated.dao;

import com.geo.integrated.model.DataMine;

public interface DataMineMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DataMine record);

    int insertSelective(DataMine record);

    DataMine selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataMine record);

    int updateByPrimaryKey(DataMine record);
}