package com.example.amscopy.control;

import com.example.amscopy.dto.ResponseEntity;
import com.example.amscopy.dto.UserLoginDto;
import com.example.amscopy.service.api.UserService;
import com.example.amscopy.utils.WebSessionUtil;
import com.example.amscopy.utils.json.RequestJson;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Slf4j
@Validated
public class LoginController {

    @Value("${login.captcha.check}")
    private boolean checkCaptcha;

    @Value("${login.password.check}")
    private boolean checkPassword;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/captcha")
    @ApiOperation(value = "验证码获取", notes = "验证码获取")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        WebSessionUtil.saveCaptcha(request, 0);
        // TODO
        ServletOutputStream out = response.getOutputStream();
        out.write(byteArrayOutputStream.toByteArray());
        out.flush();
        out.close();
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "用户登录，入参实例：{}")
    @ApiImplicitParams({@ApiImplicitParam(name = "jsonObject", value = "JSONObject", required = false, paramType = "body")})
    public ResponseEntity login(HttpServletRequest request,
                                @ApiIgnore @RequestJson @NotBlank(message = "用户名不能为空") String loginName,
                                @ApiIgnore @RequestJson @NotBlank(message = "密码不能为空") @Length(min = 6, message = "密码长度至少6位") String password,
                                @ApiIgnore @RequestJson @NotBlank(message = "验证码不能为空") Integer captcha
    ) {
        UserLoginDto login = userService.login(loginName, password, captcha);
        WebSessionUtil.saveLoginUser(request, login);
        return new ResponseEntity().success("用户登录成功", login);
    }

    @PostMapping(value = "/logout")
    @ApiOperation(value = "退出登录",notes = "退出登录")
    public ResponseEntity logout(HttpServletRequest request,HttpServletResponse response){
        WebSessionUtil.removeLoginUser(request);
        return new ResponseEntity().success("用户登出成功");
    }

    @PostMapping(value = "/resetPassword")
    @ApiOperation(value = "重置密码",notes = "")
    @ApiImplicitParams({@ApiImplicitParam(name = "jsonObject", value = "JSONObject", required = false, paramType = "body")})
    public ResponseEntity<String> resetUserPwd(
            @ApiIgnore @RequestJson @NotBlank(message = "原密码不能为空") String originPwd,
            @ApiIgnore @RequestJson @NotBlank(message = "新密码不能为空") String password,
            @ApiIgnore @RequestJson @NotBlank(message = "确认密码不能为空") String confirmPwd
    ){
        ArrayList<Object> list = new ArrayList<>();
        //按照hash值分组
        Map<Integer, List<Object>> collect = list.stream().collect(Collectors.groupingBy(Object::hashCode));

        return null;
    }






}
