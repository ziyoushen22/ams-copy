package com.example.amscopy.service;

import com.example.amscopy.mapper.RoleAuthModelMapper;
import com.example.amscopy.model.RoleAuthModel;
import com.example.amscopy.service.api.RoleAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleAuthServiceImpl implements RoleAuthService {

    @Autowired
    private RoleAuthModelMapper roleAuthModelMapper;

    private Map<String, Set<String>> authMap = new HashMap<>();

    @PostConstruct
    private void initialiazeAuthMap() {
        List<RoleAuthModel> roleAuthModels = selectAll();
        Map<String, List<RoleAuthModel>> collect = roleAuthModels.stream().collect(Collectors.groupingBy(RoleAuthModel::getRoleCode));
        for (Map.Entry<String, List<RoleAuthModel>> entry : collect.entrySet()) {
            String key = entry.getKey();
            List<RoleAuthModel> value = entry.getValue();
            HashSet<String> set = new HashSet<>(16);
            for (RoleAuthModel roleAuthModel : value) {
                set.add(roleAuthModel.getAction());
            }
            authMap.put(key, set);
        }
    }

    @Override
    public List<RoleAuthModel> selectAll() {
        return roleAuthModelMapper.selectAll();
    }

    @Override
    public boolean auth(List<String> roleCodes, String action) {
        if (roleCodes == null || action == null) {
            return false;
        }
        for (String roleCode : roleCodes) {
            Set<String> actionSet = authMap.get(roleCode);
            if (actionSet == null) {
                return false;
            }
            if (actionSet.contains(action)) {
                return true;
            }
        }
        return false;
    }
}
