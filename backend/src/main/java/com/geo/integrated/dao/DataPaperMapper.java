package com.geo.integrated.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geo.integrated.model.DataPaper;
/**
 * @author: whtli
 * @date: 2023/01/19
 * @description: 文献信息数据持久层
 */
public interface DataPaperMapper extends BaseMapper<DataPaper> {
    int deleteByPrimaryKey(Long id);

    int insert(DataPaper record);

    int insertSelective(DataPaper record);

    DataPaper selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataPaper record);

    int updateByPrimaryKey(DataPaper record);
}