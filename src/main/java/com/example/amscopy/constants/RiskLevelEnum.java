package com.example.amscopy.constants;

public enum RiskLevelEnum {
    //极高风险，高风险，中高风险，中风险，,中低风险，低风险，极低风险
    //[1,2) 极高风险

    TOP_HIGH(1, "极高风险"),
    HIGH(2, "高风险"),
    MIDDLE_HIGH(3, "中高风险"),
    MIDDLE(4, "中风险"),
    MIDDLE_LOW(5, "中低风险"),
    LOW(6, "低风险"),
    TOP_LOW(7, "极低风险");

    public static String toRiskLevelValue(int code) {
        for (RiskLevelEnum riskLevelEnum : RiskLevelEnum.values()) {
            if (riskLevelEnum.getCode() == code) {
                return riskLevelEnum.getValue();
            }
        }
        return null;
    }

    public static int toRiskLevelEnumCode(String name) {
        for (RiskLevelEnum riskLevelEnum : RiskLevelEnum.values()) {
            if (riskLevelEnum.getValue().equalsIgnoreCase(name)) {
                return riskLevelEnum.getCode();
            }
        }
        return -1;
    }

    private int code;
    private String value;

    RiskLevelEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
