package com.geo.integrated.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geo.integrated.dao.DataPaperMapper;
import com.geo.integrated.model.DataPaper;
import com.geo.integrated.service.DataPaperService;
import org.springframework.stereotype.Service;

/**
 * @author: whtli
 * @date: 2023/01/20
 * @description: 文献信息数据业务实现层
 */
@Service
public class DataPaperServiceImpl extends ServiceImpl<DataPaperMapper, DataPaper> implements DataPaperService {
}
