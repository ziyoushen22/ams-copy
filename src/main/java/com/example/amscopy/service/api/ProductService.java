package com.example.amscopy.service.api;

import com.example.amscopy.dto.PageInfo;
import com.example.amscopy.dto.product.ProductReleaseReqDto;
import com.example.amscopy.dto.product.ProductResDto;

public interface ProductService {

    void get(ProductReleaseReqDto releaseReqDto);

    PageInfo<ProductResDto> list(Integer pageNum,Integer pageSize);



}
