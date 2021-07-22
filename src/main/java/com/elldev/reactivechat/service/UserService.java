package com.elldev.reactivechat.service;

import com.elldev.reactivechat.dto.UserDto;

public interface UserService {
    UserDto register(UserDto userDto) throws Exception;
}
