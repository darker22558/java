package com.geo.integrated.controller.management;

import com.geo.integrated.annotation.OperationLogger;
import com.geo.integrated.common.Result;
import com.geo.integrated.model.vo.VisitorInfoSummary;
import com.geo.integrated.service.VisualStatisticService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: whtli
 * @date: 2023/02/09
 * @description: 网站数据统计控制层
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
     * @return 访问量信息
     */
    @ApiOperation("获取统计数据")
    @OperationLogger("获取统计数据")
    @GetMapping("/getStatisticalData")
    public Result getStatisticalData() {
        try {
            VisitorInfoSummary visitorInfoSummary = visualStatisticService.getStatisticalData();
            return Result.success("统计信息获取成功", visitorInfoSummary);
        } catch (Exception e) {
            log.error("统计信息获取失败 === {}", e.getMessage());
            return Result.fail("统计信息获取失败");
        }
    }

}
