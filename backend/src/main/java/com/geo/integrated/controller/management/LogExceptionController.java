package com.geo.integrated.controller.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geo.integrated.annotation.OperationLogger;
import com.geo.integrated.common.Result;
import com.geo.integrated.entity.LogException;
import com.geo.integrated.service.LogExceptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/01/29
 * @description: 异常日志管理控制层
 */
@Api(tags = "LogExceptionController")
@RestController
@RequestMapping("/management/log/exception")
public class LogExceptionController {
    @Autowired
    private LogExceptionService logExceptionService;
    /**
     * 查询异常日志
     *
     * @param description 按异常描述模糊查询
     * @param pageNum     页码
     * @param pageSize    每页个数
     * @return 异常日志列表
     */
    @ApiOperation("查询异常日志列表")
    @OperationLogger("查询异常日志列表")
    @GetMapping("/getExceptionLogList")
    public Result getExceptionLogList(@RequestParam(value = "description", defaultValue = "") String description,
                                      @RequestParam(value = "error", defaultValue = "") String error,
                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        QueryWrapper<LogException> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("description", description);
        queryWrapper.like("error", error);
        // 根据创建时间查询逆序的列表结果，越新发布的博客越容易被看到
        queryWrapper.orderByDesc("create_time");
        // 新建一个分页规则，pageNum代表当前页码，pageSize代表每页数量
        Page<LogException> page = new Page<>(pageNum, pageSize);
        // 借助Page实现分页查询，借助QueryWrapper实现多参数查询
        IPage<LogException> pageData = logExceptionService.page(page, queryWrapper);

        Map<String, Object> data = new HashMap<>(2);
        data.put("exceptionLogList", pageData.getRecords());
        data.put("total", pageData.getTotal());
        return Result.success("查询成功", data);
    }

    /**
     * 删除异常日志
     *
     * @param id 指定异常日志id
     * @return 删除操作的结果
     */
    @ApiOperation("删除指定异常日志")
    @OperationLogger("删除指定异常日志")
    @DeleteMapping("/deleteExceptionLogById")
    public Result deleteExceptionLogById(@RequestParam Long id) {
        boolean delete = logExceptionService.removeById(id);
        if (delete) {
            return Result.success("删除成功", id);
        } else {
            return Result.success("删除失败", id);
        }
    }


}
