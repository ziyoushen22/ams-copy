package com.example.amscopy.utils;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Slf4j
public class ExcelUtils {

    public static OutputStream getOutputStream(String fileName, HttpServletResponse response) throws Exception {
        try {
            String s = new String(fileName.getBytes("utf-8"), "ISO8859-1");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            //xls xlsx
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "no-store=0");
            response.setHeader("Cache-Control", "max-age=0");
            return response.getOutputStream();
        } catch (IOException e) {
            throw new Exception("导出文件失败");
        }
    }

    public static void writeExcel(HttpServletResponse response, List<? extends BaseRowModel> list, String fileName,
                                  String sheetName, Class clazz) throws Exception {
        ExcelWriter excelWriter = new ExcelWriter(getOutputStream(fileName, response), ExcelTypeEnum.XLSX);
        Sheet sheet = new Sheet(1, 0, clazz);
        sheet.setSheetName(sheetName);
        excelWriter.write(list, sheet);
        excelWriter.finish();

    }

}
