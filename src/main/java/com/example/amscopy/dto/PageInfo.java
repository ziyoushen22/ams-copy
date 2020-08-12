package com.example.amscopy.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("分页相应信息")
public class PageInfo<T> {

    protected Integer pageNo;
    protected Integer pageSize;
    protected Long total;
    protected List<T> rows;

}
