package com.geo.integrated.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * @author: whtli
 * @date: 2023/03/21
 * @description: 如果需要自定义线程池执行，只需要新加一个实现SchedulingConfigurer接口的 configureTasks 的类即可，这个类需要加上 @Configuration 注解。
 */
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
    private final int POOL_SIZE = 10;
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

        threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
        threadPoolTaskScheduler.setThreadNamePrefix("my-scheduled-task-pool-");
        threadPoolTaskScheduler.initialize();

        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }
}
