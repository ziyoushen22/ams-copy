package com.example.amscopy.control;

import com.example.amscopy.dto.PageInfo;
import com.example.amscopy.dto.ResponseEntity;
import com.example.amscopy.dto.excel.ManagerQutaRespDto;
import com.example.amscopy.dto.group.Create;
import com.example.amscopy.dto.product.ProductReleaseReqDto;
import com.example.amscopy.service.api.ProductService;
import com.example.amscopy.utils.ExcelUtils;
import com.example.amscopy.utils.json.RequestJson;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@RestController("/product")
@Slf4j
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/test1")
    public ResponseEntity test1(@Valid @RequestBody ProductReleaseReqDto releaseReqDto) {
        productService.get(releaseReqDto);
        return new ResponseEntity().success("成功");
    }

    @PostMapping("/test2")
    public ResponseEntity test2(@Validated({Create.class}) @RequestBody ProductReleaseReqDto releaseReqDto) {
        productService.get(releaseReqDto);
        return new ResponseEntity().success("成功");
    }

    @PostMapping("/test3")
    @ApiImplicitParams({@ApiImplicitParam(name = "jsonObject", value = "JSONObject", required = false, paramType = "body")})
    public ResponseEntity test3(
            @ApiIgnore @RequestJson @NotBlank(message = "请输入您的真实姓名") String name,
            @ApiIgnore @RequestJson @NotBlank(message = "请输入手机号") @Pattern(regexp = "^[1]([3-9])[0-9]{9}$", message = "请输入正确的手机号") String mobile,
            @ApiIgnore @RequestJson @NotBlank(message = "请输入短信验证码") String msgCode
    ) {
        //参数上的校验要和@Validated搭配使用
        return new ResponseEntity().success("成功");
    }


    @PostMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "jsonObject", value = "JSONObject", required = false, paramType = "body")})
    public ResponseEntity list(
            @ApiIgnore @RequestJson @NotBlank(message = "请输入您的真实姓名") String name,
            @ApiIgnore @RequestJson @NotBlank(message = "请输入手机号") @Pattern(regexp = "^[1]([3-9])[0-9]{9}$", message = "请输入正确的手机号") String mobile,
            @ApiIgnore @RequestJson @NotBlank(message = "请输入短信验证码") String msgCode
    ) {
        //参数上的校验要和@Validated搭配使用
        return new ResponseEntity().success("成功");
    }


}
