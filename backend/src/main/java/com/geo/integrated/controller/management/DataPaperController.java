package com.geo.integrated.controller.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geo.integrated.annotation.OperationLogger;
import com.geo.integrated.common.Result;
import com.geo.integrated.entity.DataPaper;
import com.geo.integrated.service.DataPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/01/20
 * @description: 文献信息数据管理控制层
 */
@RestController
@RequestMapping("/management/paper")
public class DataPaperController {
    @Autowired
    private DataPaperService dataPaperService;

    /**
     * 查询文献信息
     *
     * @param title    文献标题
     * @param issn     标准国际刊号
     * @param pageNum  页码
     * @param pageSize 页内数量
     * @return 文献信息列表
     */
    @OperationLogger("获取文献信息列表")
    @GetMapping("/getPaperList")
    public Result getPaperList(@RequestParam(value = "title", defaultValue = "") String title,
                               @RequestParam(value = "issn", defaultValue = "") String issn,
                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        QueryWrapper<DataPaper> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", title);
        queryWrapper.like("issn", issn);
        // 新建一个分页规则，pageNum代表当前页码，pageSize代表每页数量
        Page<DataPaper> page = new Page<>(pageNum, pageSize);
        // 借助Page实现分页查询，借助QueryWrapper实现多参数查询
        IPage<DataPaper> pageData = dataPaperService.page(page, queryWrapper);

        Map<String, Object> data = new HashMap<>(2);
        data.put("paperList", pageData.getRecords());
        data.put("total", pageData.getTotal());
        if (pageData.getTotal() == 0 && pageData.getRecords().isEmpty()) {
            return Result.success("未查找到相应文献", data);
        } else {
            return Result.success("查询成功", data);
        }
    }

    /**
     * 删除文献信息
     *
     * @param id 指定文献id
     * @return 删除操作的结果
     */
    @OperationLogger("删除指定文献信息")
    @DeleteMapping("deletePaperById")
    public Result deletePaperById(@RequestParam Long id) {
        boolean delete = dataPaperService.removeById(id);
        if (delete) {
            return Result.success("文献信息删除成功", id);
        } else {
            return Result.fail("文献信息删除失败", id);
        }
    }

    /**
     * 批量删除文献信息
     *
     * @param ids 多个指定文献信息id组成的列表
     * @return 删除操作的结果
     */
    @OperationLogger("批量删除指定文献信息")
    @DeleteMapping("/deletePaperBatchByIds")
    public Result deletePaperBatchByIds(@RequestBody List<Long> ids) {
        boolean delete = dataPaperService.removeByIds(ids);
        if (delete) {
            return Result.success("文献信息批量删除成功", ids);
        } else {
            return Result.fail("文献信息批量删除失败", ids);
        }
    }

    /**
     * 新增或编辑文献信息
     *
     * @param paper 文献信息实体类
     * @return 新增或编辑结果
     */
    @OperationLogger("新增或编辑文献信息")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody DataPaper paper) {
        // 创建或更新文献记录
        boolean flag = dataPaperService.saveOrUpdate(paper);
        if (flag) {
            return Result.success("文献信息维护成功");
        } else {
            return Result.fail("文献信息维护失败");
        }
    }
}
