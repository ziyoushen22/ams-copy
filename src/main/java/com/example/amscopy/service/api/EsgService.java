package com.example.amscopy.service.api;

import com.example.amscopy.dto.ResponseEntity;

public interface EsgService {

    ResponseEntity smsNotify(String loginId);
}
