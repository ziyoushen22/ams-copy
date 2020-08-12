package com.example.amscopy.service;

import com.example.amscopy.dto.ResponseEntity;
import com.example.amscopy.service.api.EsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Semaphore;

@Service
public class EsgServiceImpl implements EsgService {
    @Autowired
    private com.example.amscopy.utils.esg.mail.EmailUtil emailUtil;

    //Semaphore是synchronized 加强版，能控制线程数量
    private final Semaphore smsNotifySemaphore = new Semaphore(100);

    @Override
    public ResponseEntity smsNotify(String loginId) {
        if (!smsNotifySemaphore.tryAcquire()) {
            return null;
        }
        try {
            //TODO
            //emailUtil.sendActivateEmail()

        } finally {
            smsNotifySemaphore.release();
        }

        return null;
    }
}
