package com.geo.integrated.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geo.integrated.entity.DataCoalfield;

import java.util.List;

/**
 * @author: whtli
 * @date: 2023/01/26
 * @description: 煤田信息数据持久层
 */
public interface DataCoalfieldMapper extends BaseMapper<DataCoalfield> {
    List<DataCoalfield> getCoalfieldInfoOfAllProvince();
}