package com.example.amscopy.dto.product;

import lombok.Data;

import java.util.Date;

@Data
public class ProductResDto {

    private String productId;
    private String productName;
    private Date createTime;
    private String createBy;


}
