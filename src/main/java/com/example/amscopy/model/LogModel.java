package com.example.amscopy.model;

import lombok.Data;

import java.util.Date;

@Data
public class LogModel {

    private String logId;
    private String sessionId;
    private String loginId;
    private String path;
    private String requestData;
    private String responseData;
    private Date requestTime;
    private Date responseTime;

}
