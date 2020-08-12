package com.example.amscopy.utils.sensitive;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.protocol.types.Field;

public class SensitiveUtil {

    public static String mobile(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return "";
        }
        return StringUtils.left(mobile, 3).concat(
                StringUtils.removeStart(
                        StringUtils.leftPad(
                                StringUtils.right(mobile, 4), StringUtils.length(mobile), "*"), "***"));
    }

    public static String uscc(String uscc) {
        if (StringUtils.isBlank(uscc)) {
            return "";
        }
        return StringUtils.left(uscc, 4).concat(
                StringUtils.removeStart(
                        StringUtils.leftPad(
                                StringUtils.right(uscc, 6), StringUtils.length(uscc), "*"), "****"));
    }

    public static String productName(String productName) {
        if (StringUtils.isBlank(productName)) {
            return "";
        }
        return StringUtils.left(productName, 1).concat("******" + StringUtils.right(productName, 2));
    }


}
