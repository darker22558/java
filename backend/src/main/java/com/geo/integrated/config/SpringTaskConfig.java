package com.geo.integrated.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author: whtli
 * @date: 2023/02/01
 * @description: SpringTask的配置
 * 只需要在配置类中添加一个@EnableScheduling注解即可开启SpringTask的定时任务能力。
 */
@Configuration
@EnableScheduling
public class SpringTaskConfig {
}
