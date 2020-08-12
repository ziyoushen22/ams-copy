package com.example.amscopy.exception;

public enum AmsErrorCode {
    PAGE_NOT_FOUnD("110","URI请求没有相应处理类"),
    LOGIN_FAILED_TOO_MANY_TIMES("111","您输入的密码错误次数已超过5次，今日已锁定！"),
    LOGIN_IN_EXPIRED("112","您的账户已经超过有效时间，不允许登录！"),
    INVALID_PARAM("301","您输入的参数有误，请检查！"),
    INVALID_CAPTCHA("302","您输入的图形验证码有误，请检查！"),
    FOUND_EXIST_USER("303","您输入的用户已经存在，请重新输入！"),
    USER_REGISTER_FAILED("304","用户注册失败！"),
    INVALID_MOBILE_OPT_CODE("305","您输入的手机验证码有误！"),
    INVALID_NAME_OR_PWD("306","用户名或密码错误，请重新输入！"),
    INCORRECT_ORIGIN_PWD("310","你输入的原密码有误，请重新输入！"),
    NEWPWD_SAME_AS_ORIPWD("311","新密码不能和原密码一样"),
    NEWPWD_CONFIRM_FAILED("312","您输入的新密码和确认密码不一致"),
    PWD_RESET_FAILED("313","密码重置失败"),
    INCORRECT_ORIGIN_MOBILE("314","你输入的原手机号码有误，请重新输入！"),
    NEW_MOBILE_SAME_AS_ORIGIN_MOBILE("315","新老手机号不能一样"),
    USER_NOT_FOUND("316","你输入的用户名不存在"),
    USER_MOBILE_VERIFY_FAILED("317","用户名和手机号不匹配，请重新输入！"),
    LOGIN_SESSION_TIMEOUT("318","会话超时，请重新登陆"),
    ACCESS_NOT_ALLOWED("319","您没有权限访问，请联系管理员"),
    OBTAIN_MOBILE_OPT_CODE_FAILED("320","获取短信验证码失败，请稍后重试！"),
    FILE_FORMAT_ERROR("321","文件格式不正确"),
    FILE_SIZE_LIMIT("322","文件大小超过限制"),
    USERNAME_FORMAT_ERROR("326","你输入的用户名格式错误，用户名必须由6-16位的大小写字母、数字、下划线组成"),
    PWD_FORMAT_ERROR("327","您输入的密码格式错误，密码必须由8-16位的字母(区分大小写)、数字组成"),
    NO_PERMISSION("331","当前用户没有访问权限"),
    FILE_NAME_ILLEGAL("344","文件名不合法"),
    FILE_IS_WRONG("345","文件不正确"),
    FILE_UPLOAD_FAILED("350","上传失败"),
    DATA_MISSED("353","数据丢失"),
    SEQ_EXHAUST_FAILED("504","序列号耗尽"),
    REMOTE_CALL_FAILED("600","远程调用失败")

    ;


    private String code;
    private String desc;

    AmsErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
