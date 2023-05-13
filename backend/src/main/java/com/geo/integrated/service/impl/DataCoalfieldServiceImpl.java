package com.geo.integrated.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geo.integrated.dao.DataCoalfieldMapper;
import com.geo.integrated.entity.DataCoalfield;
import com.geo.integrated.service.DataCoalfieldService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/01/26
 * @description: 煤田信息数据业务层实现
 */
@Service
public class DataCoalfieldServiceImpl extends ServiceImpl<DataCoalfieldMapper, DataCoalfield> implements DataCoalfieldService {
    @Resource
    DataCoalfieldMapper dataCoalfieldMapper;
    @Override
    public Map<String, Object> getCoalfieldInfoOfAllProvince() {
        List<DataCoalfield> coalfieldList = dataCoalfieldMapper.getCoalfieldInfoOfAllProvince();
        Map<String, Integer> coalfieldCountOfProvince = new HashMap<>();
        for (DataCoalfield item : coalfieldList) {
            String province = item.getProvince();
            coalfieldCountOfProvince.put(province, coalfieldCountOfProvince.getOrDefault(province, 0) + 1);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("coalfieldList", coalfieldList);
        map.put("coalfieldCountOfProvince", coalfieldCountOfProvince);
        return map;
    }
}
