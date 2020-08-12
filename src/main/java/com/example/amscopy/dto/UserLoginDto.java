package com.example.amscopy.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Slf4j
@Data
@ApiModel("用户登录返回信息")
public class UserLoginDto implements Serializable {

    private Integer userId;
    private String userName;
    private String loginId;
    private String email;
    private String mobile;
    private Integer orgId;
    private String orgName;
    private String expiredDate;
    private Date lastLoginTime;
    private Integer loginCount;
    private List<String> roleCodes;
    private Integer isActive;
    private Date updatedTime;



}
