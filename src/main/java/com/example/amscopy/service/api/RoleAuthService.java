package com.example.amscopy.service.api;

import com.example.amscopy.model.RoleAuthModel;

import java.util.List;

public interface RoleAuthService {

    List<RoleAuthModel> selectAll();

    public boolean auth(List<String> roleCode,String action);


}
