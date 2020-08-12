package com.example.amscopy.model;

import lombok.Data;

import java.util.Date;

@Data
public class RiskWarningEventsModel {
    private int id;
    private String objectId;
    private String annDt;
    private String eventId;
    private String content;
    private String compName;
    private String secId;
    private Date createTime;
    private String createBy;
    private String bondCodes;
    private String source;
}
