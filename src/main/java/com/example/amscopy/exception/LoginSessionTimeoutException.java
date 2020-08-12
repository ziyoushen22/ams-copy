package com.example.amscopy.exception;

public class LoginSessionTimeoutException extends AmsException {

    private static final long serialVersionUID = 1L;

    public LoginSessionTimeoutException(String message) {
        super(AmsErrorCode.LOGIN_SESSION_TIMEOUT.getCode(), message);
    }
}
