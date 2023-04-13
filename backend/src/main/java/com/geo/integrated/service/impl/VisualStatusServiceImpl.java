package com.geo.integrated.service.impl;

import com.geo.integrated.model.vo.SystemStatus;
import com.geo.integrated.service.VisualStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.OperatingSystem;

/**
 * @author: whtli
 * @date: 2023/04/13
 * @description: 系统状态信息业务层实现
 */
@Service
@Slf4j
public class VisualStatusServiceImpl implements VisualStatusService {
    /**
     * 获取系统状态信息
     *
     * @return 系统状态信息
     */
    @Override
    public SystemStatus getSystemState() {
        // 系统信息
        SystemInfo systemInfo = new SystemInfo();
        // 操作系统信息
        OperatingSystem operationSystemInfo = systemInfo.getOperatingSystem();
        // 硬件信息
        HardwareAbstractionLayer hardwareInfo = systemInfo.getHardware();
        /*有了代表硬件信息的对象HardwareAbstractionLayer之后，就可以获取硬件相关的信息了*/
        // 内存相关信息
        GlobalMemory memoryInfo = hardwareInfo.getMemory();
        // 内存总容量
        String totalMemory = String.valueOf(memoryInfo.getTotal());
        // 可用内存的容量
        String availableMemory = String.valueOf(memoryInfo.getAvailable());
        /*有了内存总容量和内存可用容量，就可以计算出当前内存的利用率了*/
        // CPU相关信息
        CentralProcessor processor = hardwareInfo.getProcessor();
        // CPU型号
        String processorName = processor.getProcessorIdentifier().getName();
        // 物理CPU数
        int physicalPackageCount = processor.getPhysicalPackageCount();
        //物理核心数
        int physicalProcessorCount = processor.getPhysicalProcessorCount();

        SystemStatus systemStatus = new SystemStatus(String.valueOf(systemInfo),
                String.valueOf(operationSystemInfo),
                String.valueOf(hardwareInfo),
                String.valueOf(memoryInfo),
                Long.parseLong(totalMemory) / 1000 / 1000 / 1000,
                Long.parseLong(availableMemory) / 1000 / 1000 / 1000,
                String.valueOf(processor), String.valueOf(processorName),
                Integer.parseInt(String.valueOf(physicalPackageCount)),
                Integer.parseInt(String.valueOf(physicalProcessorCount)));

        return systemStatus;
    }
}
