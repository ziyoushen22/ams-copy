package com.example.amscopy.dto.product;

import com.example.amscopy.dto.group.Create;
import com.example.amscopy.dto.group.Update;
import com.example.amscopy.utils.sensitive.SensitiveInfo;
import com.example.amscopy.utils.sensitive.SensitiveType;
import com.example.amscopy.utils.unit.AmountInfo;
import com.example.amscopy.utils.unit.AmountType;
import com.example.amscopy.utils.validator.IsInDict;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ApiModel("产品发布")
@Data
public class ProductReleaseReqDto {

    private Integer productId;

    @ApiModelProperty(value = "产品名称", required = true)
    @NotNull(message = "产品名称不能为空", groups = {Create.class, Update.class})
    @Size(max = 50, message = "产品名称不能超过50飞字符", groups = {Create.class, Update.class})
    private String productName;

    @ApiModelProperty(value = "产品期限", required = true)
    @NotNull(message = "产品期限不能为空", groups = {Create.class, Update.class})
    @IsInDict(dictGroup = "productTerm", message = "产品期限不存在")
    private String productTermCode;

    @ApiModelProperty(value = "产品类型", required = true)
    @NotNull(message = "产品类型不能为空", groups = {Create.class, Update.class})
    @IsInDict(dictGroup = "productChannel", message = "产品类型不存在")
    private String productChannelCode;

    @ApiModelProperty(value = "业绩比较基准下限", required = true)
    @NotNull(message = "业绩比较基准下限不能为空", groups = {Create.class, Update.class})
    @Digits(integer = 13, fraction = 2, message = "业绩比较基准下限格式不正确")
    private BigDecimal lowPerformanceBenchmark;

    @ApiModelProperty(value = "业绩报酬", required = true)
    @Range(max = 1, min = 0, message = "业绩报酬必须选是/否")
    @NotBlank(message = "最低金额不能为空", groups = {Create.class, Update.class})
    private Integer backendPerformanceClaim;

    @ApiModelProperty(value = "管理人", required = true)
    @NotBlank(message = "管理人不能为空", groups = {Create.class, Update.class})
    @Length(min = 1, max = 50, message = "管理人不能超过50个字符", groups = {Create.class, Update.class})
    private String manager;

    @ApiModelProperty(value = "产品成立日期", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date openDate;

    @ApiModelProperty(value = "管理人", required = true)
    @NotBlank(message = "统一社会信用代码不能为空", groups = {Create.class, Update.class})
    @Pattern(regexp = "^[a-zA-Z0-9]{18}$", message = "统一社会信用代码不符合规范", groups = {Create.class, Update.class})
    private String orgUsc;

    @NotEmpty(message = "角色Code不能为空", groups = {Create.class, Update.class})
    private List<String> roleCodes;

    @AmountInfo(value = AmountType.WAN_YUAN, numberFormat = "#0.000000")
    private String minAmountType;

    @AmountInfo(value = AmountType.NUMBER, numberFormat = "#0.0000")
    private String custodianFee;

    //对返回的信息做掩码处理
    @SensitiveInfo(SensitiveType.USCC)
    private String uscc;


    //TODO    属性待补充
}
