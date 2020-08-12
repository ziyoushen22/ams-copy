package com.example.amscopy.model;

import lombok.Data;

import java.util.Date;

@Data
public class AbsMonitorCompany {

    private Integer companyId;

    private String companyFullName;
    private String customerName;
    private String uniteCode;
    private String organizationCode;
    private String tradeRegisterCode;
    private Date startDate;
    private Date endDate;
    private Date createTime;
    private Date updateTime;
    private String origins;
    private String instId;

}
