package com.example.amscopy.service;

import com.example.amscopy.dto.PageInfo;
import com.example.amscopy.dto.product.ProductReleaseReqDto;
import com.example.amscopy.dto.product.ProductResDto;
import com.example.amscopy.mapper.ProductMapper;
import com.example.amscopy.model.ProductModel;
import com.example.amscopy.service.api.ProductService;
import com.example.amscopy.utils.PageUtil;
import com.github.pagehelper.PageHelper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public void get(ProductReleaseReqDto releaseReqDto) {

    }

    @Override
    public PageInfo<ProductResDto> list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProductModel> list = productMapper.list();
        ModelMapper mapper = new ModelMapper();
        //list中类型转换
        List<ProductResDto> productResDtoList = mapper.map(list, new TypeToken<List<ProductResDto>>() {
        }.getType());

        //list转为pageInfo(简化版),且类型实现转换
        PageInfo<ProductResDto> resPageInfo = PageUtil.convertToPage(list, new TypeToken<List<ProductResDto>>() {
        }.getType());

        return resPageInfo;
    }
}
