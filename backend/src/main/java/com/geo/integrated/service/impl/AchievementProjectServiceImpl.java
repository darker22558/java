package com.geo.integrated.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geo.integrated.dao.AchievementProjectMapper;
import com.geo.integrated.model.AchievementProject;
import com.geo.integrated.service.AchievementProjectService;
import org.springframework.stereotype.Service;

/**
 * @author: whtli
 * @date: 2023/01/28
 * @description: 科研项目信息服务层实现
 */
@Service
public class AchievementProjectServiceImpl extends ServiceImpl<AchievementProjectMapper, AchievementProject> implements AchievementProjectService {
}
