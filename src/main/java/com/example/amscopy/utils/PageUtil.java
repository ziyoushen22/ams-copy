package com.example.amscopy.utils;

import com.example.amscopy.dto.PageInfo;
import com.github.pagehelper.PageHelper;
import org.modelmapper.ModelMapper;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.List;

public class PageUtil extends PageHelper {

    public static <E, T> PageInfo<E> convertToPage(List<T> sourceList, Type listType) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return null;
        }
        //转为pageInfo信息
        com.github.pagehelper.PageInfo<T> pageInfo = new com.github.pagehelper.PageInfo<>(sourceList);
        PageInfo resultPageInfo = new PageInfo<>();
        resultPageInfo.setPageNo(pageInfo.getPageNum());
        resultPageInfo.setPageSize(pageInfo.getPageSize());
        ModelMapper mapper = new ModelMapper();
        if (listType != null) {
            resultPageInfo.setRows(mapper.map(pageInfo.getList(), listType));
        } else {
            resultPageInfo.setRows(pageInfo.getList());
        }
        return resultPageInfo;
    }
}
