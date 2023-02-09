package com.geo.integrated.controller.management;

import com.geo.integrated.annotation.OperationLogger;
import com.geo.integrated.common.Result;
import com.geo.integrated.service.VisualStatisticService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/02/09
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/management/visualization/statistic")
public class VisualStatisticController {

    @Autowired
    private VisualStatisticService visualStatisticService;

    /**
     * 获取统计数据
     *
     * @return 存放了网站各类数据信息的哈希表
     */
    @ApiOperation("获取统计数据")
    @OperationLogger("获取统计数据")
    @GetMapping("/getStatisticalData")
    public Result getStatisticalData() {
        Map<String, Object> map = new LinkedHashMap<>();
        // 项目统计数据
        map = visualStatisticService.getProjectStatistic();

        // 总PV
        int totalPageView = visualStatisticService.getTotalPageView();
        // 日PV
        int todayPageView = visualStatisticService.getTodayPageView();
        // 总UV
        int totalUniqueVisitor = visualStatisticService.getTotalUniqueVisitor();
        // 日UV
        int todayUniqueVisitor = visualStatisticService.getTodayUniqueVisitor();

         // 所获荣誉总数
        int totalHonor = visualStatisticService.getHonorCount();
        // 科研项目总数
        int totalProject = visualStatisticService.getProjectCount();
        // 发表论文总数
        // int totalPaperPublished = visualStatisticService.getPaperPublishedCount();
        // 发明专利总数
        int totalPatent = visualStatisticService.getPatentCount();

        map.put("totalPageView", totalPageView);
        map.put("todayPageView", todayPageView);
        map.put("totalUniqueVisitor", totalUniqueVisitor);
        map.put("todayUniqueVisitor", todayUniqueVisitor);

        map.put("totalHonor", totalHonor);
        map.put("totalProject", totalProject);
        // map.put("totalPaperPublished", totalPaperPublished);
        map.put("totalPatent", totalPatent);


        return Result.success("统计信息获取成功", map);
    }

}
