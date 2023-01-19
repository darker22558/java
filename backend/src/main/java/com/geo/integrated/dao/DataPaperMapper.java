package com.geo.integrated.dao;

import com.geo.integrated.model.DataPaper;

public interface DataPaperMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DataPaper record);

    int insertSelective(DataPaper record);

    DataPaper selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataPaper record);

    int updateByPrimaryKey(DataPaper record);
}