package com.example.amscopy.mapper;

import com.example.amscopy.model.RoleAuthModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleAuthModelMapper {

    int insert (RoleAuthModel roleAuthModel);

    List<RoleAuthModel> selectAll();
}
