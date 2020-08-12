package com.example.amscopy.service.api;

import com.example.amscopy.model.DictModel;

import java.util.List;

public interface DictService {
    List<DictModel> selectAll();

    List<DictModel> getListByType(String dictType);

    DictModel getByDictKey(String dictType, String dictKey);

    String getValueFromCache(String dictType, String key);

    String getKeyFromCache(String dictType, String value);

    List<DictModel> getDictListFromCache(String dictType);


}
