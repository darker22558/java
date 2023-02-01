package com.geo.integrated.controller.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geo.integrated.annotation.OperationLogger;
import com.geo.integrated.common.Result;
import com.geo.integrated.entity.AchievementProject;
import com.geo.integrated.service.AchievementProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/01/28
 * @description: 科研项目信息管理控制层
 */
@Api(tags = "AchievementProjectController", description = "科研项目信息管理控制层")
@RestController
@RequestMapping("/management/achievement/project/")
public class AchievementProjectController {
    @Autowired
    private AchievementProjectService achievementProjectService;

    /**
     * 查询项目信息
     *
     * @param title        项目名称
     * @param chargePerson 负责人
     * @param pageNum      页码
     * @param pageSize     页内数量
     * @return 项目信息列表
     */
    @ApiOperation("获取项目信息列表")
    @OperationLogger("获取项目信息列表")
    @GetMapping("/getProjectList")
    public Result getProjectList(@RequestParam(value = "title", defaultValue = "") String title,
                                   @RequestParam(value = "chargePerson", defaultValue = "") String chargePerson,
                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        QueryWrapper<AchievementProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", title);
        queryWrapper.like("charge_person", chargePerson);
        queryWrapper.orderByAsc("id");
        // 新建一个分页规则，pageNum代表当前页码，pageSize代表每页数量
        Page<AchievementProject> page = new Page<>(pageNum, pageSize);
        // 借助Page实现分页查询，借助QueryWrapper实现多参数查询
        IPage<AchievementProject> pageData = achievementProjectService.page(page, queryWrapper);

        Map<String, Object> data = new HashMap<>(2);
        data.put("projectList", pageData.getRecords());
        data.put("total", pageData.getTotal());
        if (pageData.getTotal() == 0 && pageData.getRecords().isEmpty()) {
            return Result.success("未查找到相应项目数据", data);
        } else {
            return Result.success("查询成功", data);
        }
    }

    /**
     * 删除项目信息
     *
     * @param id 指定项目信息id
     * @return 删除操作的结果
     */
    @ApiOperation("删除指定项目信息")
    @OperationLogger("删除指定项目信息")
    @DeleteMapping("deleteProjectById")
    public Result deleteProjectById(@RequestParam Long id) {
        boolean delete = achievementProjectService.removeById(id);
        if (delete) {
            return Result.success("项目信息删除成功", id);
        } else {
            return Result.fail("项目信息删除失败", id);
        }
    }

    /**
     * 批量删除项目信息
     *
     * @param ids 多个指定项目信息id组成的列表
     * @return 删除操作的结果
     */
    @ApiOperation("批量删除指定项目信息")
    @OperationLogger("批量删除指定项目信息")
    @DeleteMapping("/deleteProjectBatchByIds")
    public Result deleteProjectBatchByIds(@RequestBody List<Long> ids) {
        boolean delete = achievementProjectService.removeByIds(ids);
        if (delete) {
            return Result.success("项目信息批量删除成功", ids);
        } else {
            return Result.fail("项目信息批量删除失败", ids);
        }
    }

    /**
     * 新增或编辑项目信息
     *
     * @param project 项目信息实体类
     * @return 新增或编辑结果
     */
    @ApiOperation("新增或编辑项目信息")
    @OperationLogger("新增或编辑项目信息")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody AchievementProject project) {
        // 创建或更新文献记录
        boolean flag = achievementProjectService.saveOrUpdate(project);
        if (flag) {
            return Result.success("项目信息维护成功");
        } else {
            return Result.fail("项目信息维护失败");
        }
    }
}
