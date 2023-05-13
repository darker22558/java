package com.geo.integrated.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.geo.integrated.entity.DataCoalfield;

import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/01/26
 * @description: 煤田信息数据业务层
 */
public interface DataCoalfieldService extends IService<DataCoalfield> {
    Map<String, Object> getCoalfieldInfoOfAllProvince();
}
