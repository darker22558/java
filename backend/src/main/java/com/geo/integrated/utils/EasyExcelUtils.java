package com.geo.integrated.utils;

import cn.hutool.core.convert.Convert;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author: whtli
 * @date: 2023/02/13
 * @description:
 */
@Slf4j
public class EasyExcelUtils {

    /**
     * 借助EasyExcel实现数据导入
     *
     * @param file  前端导入的文件
     * @param clazz 对象实例
     * @param <T>   泛型
     * @return
     */
    public static <T> List<T> importDataBatch(MultipartFile file, String fileName, Class<T> clazz) {
        log.info("数据导入，待导入文件名为 === {}", fileName);
        try {
            List<T> list = EasyExcel.read(file.getInputStream())
                    .head(clazz)
                    .sheet()
                    .doReadSync();
            return Convert.toList(clazz, list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 借助EasyExcel实现数据导出
     * 可以导出模板文件，或者导出查询后得到的数据列表
     *
     * @param response  响应头
     * @param fileName  文件名
     * @param sheetName sheet名
     * @param list      需要导出的信息列表，如果导出的是模板，则参数值为空
     * @param clazz     具体实体类的class
     */
    public static void exportDataBatch(HttpServletResponse response, String fileName, String sheetName, List<?> list, Class<?> clazz) {
        log.info("数据导出，待导出文件名为 === {}", fileName);
        try {
            // 设置响应
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // URLEncoder.encode可以防止中文乱码（和easyexcel没有关系）
            fileName = URLEncoder.encode(fileName, "utf-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 写入数据
            EasyExcel.write(response.getOutputStream(), clazz)
                    .head(clazz)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet(sheetName)
                    .doWrite(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
