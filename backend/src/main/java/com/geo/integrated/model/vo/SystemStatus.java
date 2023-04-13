package com.geo.integrated.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: whtli
 * @date: 2023/04/13
 * @description: 系统状态信息VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemStatus {
    /**
     * 系统信息
     */
    private String systemInfo;
    /**
     * 操作系统信息
     */
    private String operationSystemInfo;
    /**
     * 硬件信息
     */
    private String hardwareInfo;
    /**
     * 内存相关信息
     */
    private String memoryInfo;
    /**
     * 内存总容量
     */
    private Long totalMemory;
    /**
     * 可用内存的容量
     */
    private Long availableMemory;
    /**
     * CPU相关信息
     */
    private String processor;
    /**
     * CPU型号
     */
    private String processorName;
    /**
     * 物理CPU数
     */
    private Integer physicalPackageCount;
    /**
     * 物理核心数
     */
    private Integer physicalProcessorCount;
}
