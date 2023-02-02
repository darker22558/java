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
        log.info(countString);
        count ++;
        return countString;
    }
}
