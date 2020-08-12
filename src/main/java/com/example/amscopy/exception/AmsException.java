package com.example.amscopy.exception;

import lombok.Data;

@Data
public class AmsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String code;

    public AmsException(String message, String code) {
        super(message);
        this.code = code;
    }


}
