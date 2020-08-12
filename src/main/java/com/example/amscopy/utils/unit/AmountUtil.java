package com.example.amscopy.utils.unit;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class AmountUtil {
    private static final String WAN_YUAN = "0000";

    public static Object asWanYuanUnit(Object value, boolean roundHalfUp, String numberFormat) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return calc((BigDecimal) value, roundHalfUp);
        } else {
            DecimalFormat df = new DecimalFormat(numberFormat);
            return df.format(calc(new BigDecimal((String) value), roundHalfUp));
        }
    }

    public static String asFormatString(Object value, String numberFormat) {
        if (value == null) {
            return null;
        }
        DecimalFormat df = new DecimalFormat(numberFormat);
        if (value instanceof BigDecimal) {
            return df.format(value);
        } else {
            return df.format(new BigDecimal((String) value));
        }
    }

    private static BigDecimal calc(BigDecimal num, boolean roundHalfUp) {
        return num.divide(new BigDecimal(WAN_YUAN), 6, roundHalfUp ? BigDecimal.ROUND_HALF_UP : BigDecimal.ROUND_HALF_DOWN);
    }
}
