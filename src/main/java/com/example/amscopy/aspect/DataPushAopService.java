package com.example.amscopy.aspect;

import com.example.amscopy.dto.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Aspect
public class DataPushAopService {

    @Autowired
    private MercuryPostService mercuryPostService;

    @Pointcut("execution(* com.example.amscopy.control.LoginController.login(..))")
    public void addUserData(){
    }

    @AfterReturning(value = "addUserData()",returning = "entity")
    public void addUser(JoinPoint point, ResponseEntity entity)throws Throwable{
        mercuryPostService.updateMercuryUser(null);

    }

}
