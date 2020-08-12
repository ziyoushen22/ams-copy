package com.example.amscopy.model;

import lombok.Data;

import java.util.Date;

@Data
public class DictModel {

    private Integer dictId;
    private String dictType;
    private String dictKey;
    private String dictValue;
    private Integer sortNo;
    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;
    private Integer delFlag;
    private String remark;

}
