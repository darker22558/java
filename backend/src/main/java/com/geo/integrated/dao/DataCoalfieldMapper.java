package com.geo.integrated.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geo.integrated.entity.DataCoalfield;

/**
 * @author: whtli
 * @date: 2023/01/26
 * @description: 煤田信息数据持久层
 */
public interface DataCoalfieldMapper extends BaseMapper<DataCoalfield> {
    int deleteByPrimaryKey(Long id);

    int insert(DataCoalfield record);

    int insertSelective(DataCoalfield record);

    DataCoalfield selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataCoalfield record);

    int updateByPrimaryKey(DataCoalfield record);
}