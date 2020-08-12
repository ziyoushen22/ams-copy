package com.example.amscopy.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.example.amscopy.dto.OutputDto;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("管理人指标")
public class ManagerQutaRespDto extends BaseRowModel implements OutputDto {

    private String productId;

    @ExcelProperty(value = "在管产品名称", index = 0)
    private String productName;

    @ExcelProperty(value = "产品编号", index = 1)
    private String productNum;

    @ExcelProperty(value = "基础产品类别", index = 2)
    private String assetType;

    @ExcelProperty(value = "所属机构", index = 3)
    private String orgName;

    @ExcelProperty(value = "统一社会信用代码", index = 4)
    private String orgUsc;

    @ExcelProperty(value = "更新时间", index = 5)
    private String tradeDate;

    @ExcelProperty(value = "管理规模", index = 6)
    private String capitalTotal;
}
