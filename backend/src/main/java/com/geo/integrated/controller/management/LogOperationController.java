package com.geo.integrated.controller.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geo.integrated.annotation.OperationLogger;
import com.geo.integrated.common.Result;
import com.geo.integrated.model.LogOperation;
import com.geo.integrated.service.LogOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/01/27
 * @description: 操作日志管理控制层
 */
@RestController
@RequestMapping("/management/log")
public class LogOperationController {
    @Autowired
    private LogOperationService logOperationService;

    /**
     * 查询操作日志
     *
     * @param description 按操作描述模糊查询
     * @param pageNum     页码
     * @param pageSize    每页个数
     * @return 操作日志列表
     */
    @OperationLogger("查询操作日志列表")
    @GetMapping("/getOperationLogList")
    public Result getOperationLogList(@RequestParam(defaultValue = "") String description,
                                      @RequestParam(defaultValue = "1") Integer pageNum,
                                      @RequestParam(defaultValue = "10") Integer pageSize) {
        QueryWrapper<LogOperation> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("description", description);
        // 根据创建时间查询逆序的列表结果，越新发布的博客越容易被看到
        queryWrapper.orderByDesc("create_time");
        // 新建一个分页规则，pageNum代表当前页码，pageSize代表每页数量
        Page<LogOperation> page = new Page<>(pageNum, pageSize);
        // 借助Page实现分页查询，借助QueryWrapper实现多参数查询
        IPage<LogOperation> pageData = logOperationService.page(page, queryWrapper);

        Map<String, Object> data = new HashMap<>(2);
        data.put("operationLogList", pageData.getRecords());
        data.put("total", pageData.getTotal());
        return Result.success("查询成功", data);
    }

    /**
     * 删除操作日志
     *
     * @param id 指定操作日志id
     * @return 删除操作的结果
     */
    @OperationLogger("删除指定操作日志")
    @DeleteMapping("/deleteOperationLogById")
    public Result delete(@RequestParam Long id) {
        boolean delete = logOperationService.removeById(id);
        if (delete) {
            return Result.success("删除成功", id);
        } else {
            return Result.success("删除失败", id);
        }
    }
}
