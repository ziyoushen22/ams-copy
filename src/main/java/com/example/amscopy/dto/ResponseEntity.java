package com.example.amscopy.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "响应类", description = "返回信息")
public class ResponseEntity<T> {

    @ApiModelProperty(value = "状态码 000-成功")
    private String rtnCode;

    @ApiModelProperty(value = "状态信息")
    private String rtnMsg;

    @ApiModelProperty(value = "数据对象")
    private T data;

    public ResponseEntity fail(String code, String msg) {
        this.rtnCode = code;
        this.rtnMsg = msg;
        return this;
    }

    public ResponseEntity fail(String msg) {
        this.rtnCode = "-1";
        this.rtnMsg = msg;
        return this;
    }

    public ResponseEntity success(String msg) {
        this.rtnCode = "000";
        this.rtnMsg = msg;
        return this;
    }

    public ResponseEntity success(String msg, T data) {
        this.rtnCode = "000";
        this.rtnMsg = msg;
        this.data = data;
        return this;
    }

    public ResponseEntity warn(String msg) {
        this.rtnCode = "999";
        this.rtnMsg = msg;
        return this;
    }

    public boolean isSucceed() {
        return "000".equalsIgnoreCase(rtnCode);
    }

}
