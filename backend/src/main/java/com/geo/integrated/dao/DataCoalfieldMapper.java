package com.geo.integrated.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geo.integrated.model.DataCoalfield;

public interface DataCoalfieldMapper extends BaseMapper<DataCoalfield> {
    int deleteByPrimaryKey(Long id);

    int insert(DataCoalfield record);

    int insertSelective(DataCoalfield record);

    DataCoalfield selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataCoalfield record);

    int updateByPrimaryKey(DataCoalfield record);
}