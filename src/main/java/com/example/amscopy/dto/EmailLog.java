package com.example.amscopy.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EmailLog {

    private Integer id;
    private String from;
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String content;
    private Date sendDate;
    private Integer status;
    private String error;
}
