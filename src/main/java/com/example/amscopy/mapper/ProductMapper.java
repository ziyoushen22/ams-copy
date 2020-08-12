package com.example.amscopy.mapper;

import com.example.amscopy.model.ProductModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<ProductModel> list();

}
