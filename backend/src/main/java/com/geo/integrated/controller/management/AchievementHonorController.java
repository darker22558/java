package com.geo.integrated.controller.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geo.integrated.annotation.OperationLogger;
import com.geo.integrated.common.Result;
import com.geo.integrated.entity.AchievementHonor;
import com.geo.integrated.service.AchievementHonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/01/28
 * @description: 所获荣誉信息管理控制层
 */
@RestController
@RequestMapping("/management/achievement/honor/")
public class AchievementHonorController {
    @Autowired
    private AchievementHonorService achievementHonorService;

    /**
     * 查询荣誉信息
     *
     * @param honorName        所获荣誉名称
     * @param achievementName  获奖成果名称
     * @param participantsRank 实验室人员排名
     * @param pageNum          页码
     * @param pageSize         页内数量
     * @return 荣誉信息列表
     */
    @OperationLogger("获取荣誉信息列表")
    @GetMapping("/getHonorList")
    public Result getHonorList(@RequestParam(value = "honorName", defaultValue = "") String honorName,
                               @RequestParam(value = "achievementName", defaultValue = "") String achievementName,
                               @RequestParam(value = "participantsRank", defaultValue = "") String participantsRank,
                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        QueryWrapper<AchievementHonor> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("honor_name", honorName);
        queryWrapper.like("achievement_name", achievementName);
        queryWrapper.like("participants_rank", participantsRank);
        // 新建一个分页规则，pageNum代表当前页码，pageSize代表每页数量
        Page<AchievementHonor> page = new Page<>(pageNum, pageSize);
        // 借助Page实现分页查询，借助QueryWrapper实现多参数查询
        IPage<AchievementHonor> pageData = achievementHonorService.page(page, queryWrapper);

        Map<String, Object> data = new HashMap<>(2);
        data.put("honorList", pageData.getRecords());
        data.put("total", pageData.getTotal());
        if (pageData.getTotal() == 0 && pageData.getRecords().isEmpty()) {
            return Result.success("未查找到相应荣誉数据", data);
        } else {
            return Result.success("查询成功", data);
        }
    }

    /**
     * 删除荣誉信息
     *
     * @param id 指定荣誉信息id
     * @return 删除操作的结果
     */
    @OperationLogger("删除指定荣誉信息")
    @DeleteMapping("deleteHonorById")
    public Result deleteHonorById(@RequestParam Long id) {
        boolean delete = achievementHonorService.removeById(id);
        if (delete) {
            return Result.success("荣誉信息删除成功", id);
        } else {
            return Result.fail("荣誉信息删除失败", id);
        }
    }

    /**
     * 批量删除荣誉信息
     *
     * @param ids 多个指定荣誉信息id组成的列表
     * @return 删除操作的结果
     */
    @OperationLogger("批量删除指定荣誉信息")
    @DeleteMapping("/deleteHonorBatchByIds")
    public Result deleteHonorBatchByIds(@RequestBody List<Long> ids) {
        boolean delete = achievementHonorService.removeByIds(ids);
        if (delete) {
            return Result.success("荣誉信息批量删除成功", ids);
        } else {
            return Result.fail("荣誉信息批量删除失败", ids);
        }
    }

    /**
     * 新增或编辑荣誉信息
     *
     * @param honor 荣誉信息实体类
     * @return 新增或编辑结果
     */
    @OperationLogger("新增或编辑荣誉信息")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody AchievementHonor honor) {
        // 创建或更新文献记录
        boolean flag = achievementHonorService.saveOrUpdate(honor);
        if (flag) {
            return Result.success("荣誉信息维护成功");
        } else {
            return Result.fail("荣誉信息维护失败");
        }
    }
}
