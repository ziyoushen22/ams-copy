package com.example.amscopy.service;

import com.example.amscopy.model.DictModel;
import com.example.amscopy.service.api.DictService;

import java.util.List;

public class DictServiceImpl implements DictService {
    @Override
    public List<DictModel> selectAll() {
        return null;
    }

    @Override
    public List<DictModel> getListByType(String dictType) {
        return null;
    }

    @Override
    public DictModel getByDictKey(String dictType, String dictKey) {
        return null;
    }

    @Override
    public String getValueFromCache(String dictType, String key) {
        return null;
    }

    @Override
    public String getKeyFromCache(String dictType, String value) {
        return null;
    }

    @Override
    public List<DictModel> getDictListFromCache(String dictType) {
        return null;
    }
}
