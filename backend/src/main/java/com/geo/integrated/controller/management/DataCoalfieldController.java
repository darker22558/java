package com.geo.integrated.controller.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geo.integrated.annotation.OperationLogger;
import com.geo.integrated.common.Result;
import com.geo.integrated.model.DataCoalfield;
import com.geo.integrated.model.DataPaper;
import com.geo.integrated.service.DataCoalfieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/01/26
 * @description: 煤田信息数据管理控制层
 */
@RestController
@RequestMapping("/management/coalfield")
public class DataCoalfieldController {
    @Autowired
    private DataCoalfieldService dataCoalfieldService;

    /**
     * 查询煤田信息
     *
     * @param coalfieldName    煤田名称
     * @param coalCoveringArea 聚煤区
     * @param pageNum          页码
     * @param pageSize         页内数量
     * @return 煤田信息列表
     */
    @OperationLogger("获取煤田信息列表")
    @GetMapping("/getCoalfieldList")
    public Result getCoalfieldList(@RequestParam(value = "coalfieldName", defaultValue = "") String coalfieldName,
                                   @RequestParam(value = "coalCoveringArea", defaultValue = "") String coalCoveringArea,
                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        QueryWrapper<DataCoalfield> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("coalfield_name", coalfieldName);
        queryWrapper.like("coal_covering_area", coalCoveringArea);
        // 新建一个分页规则，pageNum代表当前页码，pageSize代表每页数量
        Page<DataCoalfield> page = new Page<>(pageNum, pageSize);
        // 借助Page实现分页查询，借助QueryWrapper实现多参数查询
        IPage<DataCoalfield> pageData = dataCoalfieldService.page(page, queryWrapper);

        Map<String, Object> data = new HashMap<>(2);
        data.put("coalfieldList", pageData.getRecords());
        data.put("total", pageData.getTotal());
        if (pageData.getTotal() == 0 && pageData.getRecords().isEmpty()) {
            return Result.success("未查找到相应煤田数据", data);
        } else {
            return Result.success("查询成功", data);
        }
    }

    /**
     * 删除煤田信息
     *
     * @param id 指定煤田信息id
     * @return 删除操作的结果
     */
    @OperationLogger("删除指定煤田信息")
    @DeleteMapping("deleteCoalfieldById")
    public Result deleteCoalfieldById(@RequestParam Long id) {
        boolean delete = dataCoalfieldService.removeById(id);
        if (delete) {
            return Result.success("煤田信息删除成功", id);
        } else {
            return Result.fail("煤田信息删除失败", id);
        }
    }

    /**
     * 批量删除煤田信息
     *
     * @param ids 多个指定煤田信息id组成的列表
     * @return 删除操作的结果
     */
    @OperationLogger("批量删除指定煤田信息")
    @DeleteMapping("/deleteCoalfieldBatchByIds")
    public Result deleteCoalfieldBatchByIds(@RequestBody List<Long> ids) {
        boolean delete = dataCoalfieldService.removeByIds(ids);
        if (delete) {
            return Result.success("煤田信息批量删除成功", ids);
        } else {
            return Result.fail("煤田信息批量删除失败", ids);
        }
    }

    /**
     * 新增或编辑煤田信息
     *
     * @param coalfield 煤田信息实体类
     * @return 新增或编辑结果
     */
    @OperationLogger("新增或编辑煤田信息")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody DataCoalfield coalfield) {
        // 创建或更新文献记录
        boolean flag = dataCoalfieldService.saveOrUpdate(coalfield);
        if (flag) {
            return Result.success("煤田信息维护成功");
        } else {
            return Result.fail("煤田信息维护失败");
        }
    }
}
