package com.example.amscopy.control;

import com.example.amscopy.dto.PageInfo;
import com.example.amscopy.dto.ResponseEntity;
import com.example.amscopy.dto.excel.ManagerQutaRespDto;
import com.example.amscopy.service.api.ExcelService;
import com.example.amscopy.utils.ExcelUtils;
import com.example.amscopy.utils.WebSessionUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "/file")
@Slf4j
@Validated
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @PostMapping("/excelDownload")
    @ApiImplicitParams({@ApiImplicitParam(name = "jsonObject", value = "JSONObject", required = false, paramType = "body")})
    public void test4(HttpServletResponse response) {
        //填充数据 rows
        ResponseEntity<PageInfo> responseEntity = new ResponseEntity<>();
        List<ManagerQutaRespDto> rows = ((PageInfo) responseEntity.getData()).getRows();
        try {
            String fileName = "管理人统计指标";
            String sheetName = "sheet1";
            ExcelUtils.writeExcel(response, rows, fileName, sheetName, ManagerQutaRespDto.class);
        } catch (Exception e) {
            log.error("模板下载失败", e);
        }
    }

    @PostMapping("/handlerFileUpload")
    @ApiImplicitParams({@ApiImplicitParam(name = "jsonObject", value = "JSONObject", required = false, paramType = "body")})
    public ResponseEntity handlerFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        Integer orgId = WebSessionUtil.getOrgId(request);
        String loginId = WebSessionUtil.getLoginId(request);
        excelService.batchImport(file.getInputStream(), orgId, loginId);
        return new ResponseEntity().success("成功");
    }

}
