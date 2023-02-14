package com.geo.integrated.controller.management;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geo.integrated.annotation.OperationLogger;
import com.geo.integrated.common.Constant;
import com.geo.integrated.common.Result;
import com.geo.integrated.entity.DataPaper;
import com.geo.integrated.service.DataPaperService;
import com.geo.integrated.utils.EasyExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/01/20
 * @description: 文献信息数据管理控制层
 */
@Api(tags = "DataPaperController")
@Slf4j
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
     * 导出文献数据模板
     *
     * @param response 响应信息
     * @return 文献数据模板
     */
    @ApiOperation("导出文献数据模板")
    @OperationLogger("导出文献数据模板")
    @GetMapping("/exportDataTemplate")
    public void exportDataTemplate(HttpServletResponse response) {
        String fileName = "文献数据模板";
        String sheetName = "sheet1";
        EasyExcelUtils.exportDataBatch(response, fileName, sheetName, null, DataPaper.class);
    }

    /**
     * 批量导入文献数据信息
     *
     * @param file 携带多条文献信息的excel文件
     * @return 导入结果
     */
    @ApiOperation("批量导入文献数据")
    @OperationLogger("批量导入文献数据")
    @PostMapping("/importDataBatch")
    public Result importDataBatch(@RequestPart(value = "file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String fileType = FileUtil.extName(fileName);
        if (Constant.FILE_TYPE_XLSX.equals(fileType) || Constant.FILE_TYPE_XLS.equals(fileType)) {
            List<DataPaper> list = EasyExcelUtils.importDataBatch(file, fileName, DataPaper.class);
            log.info("待导入的数据 === {}", list);

            boolean upload = dataPaperService.saveBatch(list);
            if (upload) {
                return Result.success("easy excel 导入成功", list);
            } else {
                return Result.fail("easy excel 导入失败", list);
            }
        } else {
            return Result.fail("文件类型错误，应为.xlsx或.xls文件");
        }
    }

    /**
     * 批量导出文献数据信息
     *
     * @param response 响应信息
     * @return 文献数据信息列表
     */
    @ApiOperation("批量导出文献数据")
    @OperationLogger("批量导出文献数据")
    @GetMapping("/exportDataBatch")
    public void exportDataBatch(@RequestParam(value = "title", defaultValue = "") String title,
                                @RequestParam(value = "issn", defaultValue = "") String issn,
                                HttpServletResponse response) {
        // 设置查询条件然后获取数据
        QueryWrapper<DataPaper> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", title);
        queryWrapper.like("issn", issn);
        List<DataPaper> list = dataPaperService.list(queryWrapper);
        log.info("待下载的数据量 === {}条", list.size());

        String fileName = "文献数据信息";
        String sheetName = "sheet1";
        EasyExcelUtils.exportDataBatch(response, fileName, sheetName, list, DataPaper.class);
    }
}
