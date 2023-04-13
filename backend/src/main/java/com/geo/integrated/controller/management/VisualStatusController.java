package com.geo.integrated.controller.management;

import com.geo.integrated.annotation.OperationLogger;
import com.geo.integrated.common.Result;
import com.geo.integrated.exception.ServiceException;
import com.geo.integrated.model.vo.SystemStatus;
import com.geo.integrated.service.VisualStatisticService;
import com.geo.integrated.service.VisualStatusService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/02/09
 * @description: 系统状态信息控制层
 */
@Slf4j
@RestController
@RequestMapping("/management/visualization/status")
public class VisualStatusController {

    @Autowired
    private VisualStatusService visualStatusService;

    /**
     * 获取系统状态信息
     *
     * @return 存放了系统状态信息的哈希表
     */
    @ApiOperation("获取系统状态信息")
    @OperationLogger("获取系统状态信息")
    @GetMapping("/getSystemState")
    public Result getSystemState() {
        try {
            SystemStatus systemStatus = visualStatusService.getSystemState();
            return Result.success("统计系统状态获取成功", systemStatus);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("统计系统状态获取失败");
        }
    }
}
