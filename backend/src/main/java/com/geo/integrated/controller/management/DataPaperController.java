package com.geo.integrated.controller.management;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geo.integrated.annotation.OperationLogger;
import com.geo.integrated.common.Constant;
import com.geo.integrated.common.Result;
import com.geo.integrated.entity.DataPaper;
import com.geo.integrated.service.DataPaperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/01/20
 * @description: 文献信息数据管理控制层
 */
@Api(tags = "DataPaperController")
@RestController
@RequestMapping("/management/data/paper")
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
    @ApiOperation("获取文献信息列表")
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
    @ApiOperation("删除指定文献信息")
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
    @ApiOperation("批量删除指定文献信息")
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
    @ApiOperation("新增或编辑文献信息")
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

    /**
     * 下载模板
     *
     * @return 批量导入时可用的excel模板文件
     */
    @ApiOperation("下载模板")
    @OperationLogger("下载模板")
    @GetMapping("/downloadTemplate")
    public Result downloadTemplate() {
        return Result.success("");
    }

    /**
     * 批量导入文献数据
     *
     * @param file 携带多条文献信息的excel文件
     * @return 批量导入结果，若中途导入失败，则报错并返回
     * @throws IOException
     */
    @ApiOperation("批量导入文献数据")
    @OperationLogger("批量导入文献数据")
    @PostMapping("/uploadDataBatch")
    public Result uploadDataBatch(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);

        if (Constant.FILE_TYPE_XLSX.equals(type) || Constant.FILE_TYPE_XLS.equals(type)) {
            // 逐行读取记录，每行是一个博客，列名对应数据库字段名
            InputStream inputStream = file.getInputStream();
            ExcelReader reader = ExcelUtil.getReader(inputStream);
            // 通过javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
            List<DataPaper> list = reader.readAll(DataPaper.class);
            for (int i = 0; i < list.size(); i++) {
                try {
                    dataPaperService.save(list.get(i));
                } catch (Exception e) {
                    return Result.fail("批量导入失败，停止继续导入，失败行数为：" + i, e.getMessage());
                }
            }
        } else {
            return Result.fail("文件类型错误，应为.xlsx或.xls文件");
        }
        return Result.success("文献信息批量导入成功", originalFilename);
    }
}
