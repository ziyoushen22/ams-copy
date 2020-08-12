package com.example.amscopy.model;

import lombok.Data;

import java.util.Date;

@Data
public class ProductModel {

    private String productId;
    private String productName;
    private Date createTime;
    private String createBy;
}
