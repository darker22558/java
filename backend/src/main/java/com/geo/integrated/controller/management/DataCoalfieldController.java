package com.geo.integrated.controller.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 * @description:
 */
@RestController
@RequestMapping("/management/coalfield")
public class DataCoalfieldController {
    @Autowired
    private DataCoalfieldService dataCoalfieldService;

    @GetMapping("/getCoalfieldList")
    public Result getCoalfieldList(@RequestParam(value = "coalfieldName", defaultValue = "") String coalfieldName,
                                   @RequestParam(value = "coalCoveringArea", defaultValue = "") String coalCoveringArea,
                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("coalfield_name", coalfieldName);
        queryWrapper.like("coal_covering_area", coalCoveringArea);
        // 新建一个分页规则，pageNum代表当前页码，pageSize代表每页数量
        Page page = new Page(pageNum, pageSize);
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

    @DeleteMapping("deleteCoalfieldById")
    public Result deleteCoalfieldById(@RequestParam Long id) {
        boolean delete = dataCoalfieldService.removeById(id);
        if (delete) {
            return Result.success("煤田信息删除成功", id);
        } else {
            return Result.fail("煤田信息删除失败", id);
        }
    }

    @DeleteMapping("/deleteCoalfieldBatchByIds")
    public Result deleteCoalfieldBatchByIds(@RequestBody List<Long> ids) {
        boolean delete = dataCoalfieldService.removeByIds(ids);
        if (delete) {
            return Result.success("煤田信息批量删除成功", ids);
        } else {
            return Result.fail("煤田信息批量删除失败", ids);
        }
    }
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody DataCoalfield coalfield) {
        // 创建或更新文献记录
        boolean flag = dataCoalfieldService.saveOrUpdate(coalfield);
        if (flag) {
            return Result.success("煤田信息维护成功");
        }
        return Result.fail("煤田信息维护失败");
    }
}
