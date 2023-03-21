package com.geo.integrated.component;

import com.geo.integrated.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author: whtli
 * @date: 2023/02/01
 * @description: 测试计数定时器
 */
@Slf4j
@Component
public class TestCountTask {
    public Integer count = 1;
    /**
     * cron表达式：Seconds Minutes Hours DayofMonth Month DayofWeek [Year]
     * 测试：每10秒扫描一次，自动加1
     */
    @Scheduled(cron = "0/10 * * ? * ?")
    public String countTask() {
        String countString = Constant.TASK_STRING + " : " +  count;
        log.info("定时任务 === {}", countString);
        // 默认情况下，@Scheduled任务都在Spring创建的大小为1的默认线程池中执行，通过在加了@Scheduled注解的方法里加上下面这段代码来验证。
        // 如果需要自定义线程池执行，只需要新加一个实现SchedulingConfigurer接口的 configureTasks 的类，这个类需要加上 @Configuration 注解。
        log.info("Current Thread : {}", Thread.currentThread().getName());
        count ++;
        return countString;
    }
}
