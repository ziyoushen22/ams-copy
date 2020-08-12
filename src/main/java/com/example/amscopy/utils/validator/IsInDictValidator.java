package com.example.amscopy.utils.validator;

import com.example.amscopy.model.DictModel;
import com.example.amscopy.service.api.DictService;
import com.example.amscopy.utils.ApplicationContextUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsInDictValidator implements ConstraintValidator<IsInDict, String> {

    private boolean required = false;
    private String dictGroup = null;

    @Override
    public void initialize(IsInDict constraintAnnotation) {
        required = constraintAnnotation.required();
        dictGroup = constraintAnnotation.dictGroup();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(s)) {
            return true;
        }
        DictService dictService = ApplicationContextUtil.getBean(DictService.class);
        if (dictService != null) {
            DictModel dictModel = dictService.getByDictKey(dictGroup, s);
            return dictModel != null;
        }
        return !required;
    }
}
