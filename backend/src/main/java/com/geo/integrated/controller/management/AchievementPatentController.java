package com.geo.integrated.controller.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geo.integrated.annotation.OperationLogger;
import com.geo.integrated.common.Result;
import com.geo.integrated.entity.AchievementPatent;
import com.geo.integrated.service.AchievementPatentService;
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
 * @description: 发明专利信息管理控制层
 */
@Api(tags = "AchievementPatentController")
@RestController
@RequestMapping("/management/achievement/patent/")
public class AchievementPatentController {
    @Autowired
    private AchievementPatentService achievementPatentService;

    /**
     * 查询专利信息
     *
     * @param number   编号
     * @param title    专利名称
     * @param finisher 完成人
     * @param pageNum  页码
     * @param pageSize 页内数量
     * @return 专利信息列表
     */
    @ApiOperation("获取专利信息列表")
    @OperationLogger("获取专利信息列表")
    @GetMapping("/getPatentList")
    public Result getPatentList(@RequestParam(value = "number", defaultValue = "") String number,
                                @RequestParam(value = "title", defaultValue = "") String title,
                                @RequestParam(value = "finisher", defaultValue = "") String finisher,
                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        QueryWrapper<AchievementPatent> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("number", number);
        queryWrapper.like("title", title);
        queryWrapper.like("finisher", finisher);
        // 新建一个分页规则，pageNum代表当前页码，pageSize代表每页数量
        Page<AchievementPatent> page = new Page<>(pageNum, pageSize);
        // 借助Page实现分页查询，借助QueryWrapper实现多参数查询
        IPage<AchievementPatent> pageData = achievementPatentService.page(page, queryWrapper);

        Map<String, Object> data = new HashMap<>(2);
        data.put("patentList", pageData.getRecords());
        data.put("total", pageData.getTotal());
        if (pageData.getTotal() == 0 && pageData.getRecords().isEmpty()) {
            return Result.success("未查找到相应专利数据", data);
        } else {
            return Result.success("查询成功", data);
        }
    }

    /**
     * 删除专利信息
     *
     * @param id 指定专利信息id
     * @return 删除操作的结果
     */
    @ApiOperation("删除指定专利信息")
    @OperationLogger("删除指定专利信息")
    @DeleteMapping("deletePatentById")
    public Result deletePatentById(@RequestParam Long id) {
        boolean delete = achievementPatentService.removeById(id);
        if (delete) {
            return Result.success("专利信息删除成功", id);
        } else {
            return Result.fail("专利信息删除失败", id);
        }
    }

    /**
     * 批量删除专利信息
     *
     * @param ids 多个指定专利信息id组成的列表
     * @return 删除操作的结果
     */
    @ApiOperation("批量删除指定专利信息")
    @OperationLogger("批量删除指定专利信息")
    @DeleteMapping("/deletePatentBatchByIds")
    public Result deletePatentBatchByIds(@RequestBody List<Long> ids) {
        boolean delete = achievementPatentService.removeByIds(ids);
        if (delete) {
            return Result.success("专利信息批量删除成功", ids);
        } else {
            return Result.fail("专利信息批量删除失败", ids);
        }
    }

    /**
     * 新增或编辑专利信息
     *
     * @param patent 专利信息实体类
     * @return 新增或编辑结果
     */
    @ApiOperation("新增或编辑专利信息")
    @OperationLogger("新增或编辑专利信息")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody AchievementPatent patent) {
        // 创建或更新文献记录
        boolean flag = achievementPatentService.saveOrUpdate(patent);
        if (flag) {
            return Result.success("专利信息维护成功");
        } else {
            return Result.fail("专利信息维护失败");
        }
    }
}
