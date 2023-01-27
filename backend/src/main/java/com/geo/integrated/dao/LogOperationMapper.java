package com.geo.integrated.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geo.integrated.model.LogOperation;
/**
 * @author: whtli
 * @date: 2023/01/26
 * @description: 操作日志持久层
 */
public interface LogOperationMapper extends BaseMapper<LogOperation> {
    int deleteByPrimaryKey(Long id);

    int insert(LogOperation record);

    int insertSelective(LogOperation record);

    LogOperation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LogOperation record);

    int updateByPrimaryKey(LogOperation record);
}