package com.geo.integrated.controller.management;

import com.geo.integrated.annotation.OperationLogger;
import com.geo.integrated.common.Result;
import com.geo.integrated.model.vo.AcademicSummary;
import com.geo.integrated.service.VisualAcademicService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/02/09
 * @description: 学术成果信息统计控制层
 */
@Slf4j
@RestController
@RequestMapping("/management/visualization/academic")
public class VisualAcademicController {

    @Autowired
    private VisualAcademicService visualAcademicService;

    /**
     * 获取统计数据
     *
     * @return 存放了网站各类数据信息的哈希表
     */
    @ApiOperation("获取统计数据")
    @OperationLogger("获取统计数据")
    @GetMapping("/getAcademicData")
    public Result getAcademicData() {
        AcademicSummary academicSummary = visualAcademicService.getAcademicSummaryDate();
        return Result.success("统计信息获取成功", academicSummary);
    }

}
