package com.example.amscopy.service.api;

import com.example.amscopy.dto.UserLoginDto;

public interface UserService {
    UserLoginDto login (String loginName,String password ,Integer captcha);
}
