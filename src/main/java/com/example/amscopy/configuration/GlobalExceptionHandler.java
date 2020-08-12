package com.example.amscopy.configuration;

import com.example.amscopy.dto.ResponseEntity;
import com.example.amscopy.exception.AmsErrorCode;
import com.example.amscopy.exception.AmsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(HttpServletRequest request, Exception e) {
        if (e instanceof AmsException) {
            log.warn(e.getMessage(), e);
            AmsException ae = (AmsException) e;
            return new ResponseEntity().fail(ae.getCode(), ae.getMessage());
        } else if (e instanceof MethodArgumentNotValidException) {
            log.warn(e.getMessage(), e);
            MethodArgumentNotValidException me = (MethodArgumentNotValidException) e;
            BindingResult result = me.getBindingResult();
            FieldError error = result.getFieldError();
            return new ResponseEntity().fail(AmsErrorCode.INVALID_PARAM.getCode(), error.getDefaultMessage());
        } else if (e instanceof ConstraintViolationException) {
            log.warn(e.getMessage(), e);
            ConstraintViolationException ce = (ConstraintViolationException) e;
            for (ConstraintViolation<?> cv : ce.getConstraintViolations()) {
                int index = cv.getMessage().indexOf(":");
                String message = index != -1 ? cv.getMessage().substring(index + 1).trim() : cv.getMessage();
                return new ResponseEntity().fail(AmsErrorCode.INVALID_PARAM.getCode(), message);
            }
            return new ResponseEntity().fail(AmsErrorCode.INVALID_PARAM.getCode(), e.getMessage());
        } else if (e instanceof MaxUploadSizeExceededException) {
            log.warn(e.getMessage(), e);
            return new ResponseEntity().fail(AmsErrorCode.FILE_SIZE_LIMIT.getCode(), AmsErrorCode.FILE_SIZE_LIMIT.getDesc());
        } else {
            log.warn(e.getMessage(), e);
            return new ResponseEntity().fail("系统错误");
        }
    }

}
