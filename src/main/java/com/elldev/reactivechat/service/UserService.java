package com.elldev.reactivechat.service;

import com.elldev.reactivechat.dto.UserDto;
import com.elldev.reactivechat.entity.UserSession;

public interface UserService {
    UserDto signIn(UserDto userDto) throws Exception;
    UserDto signUp(UserDto userDto) throws Exception;
    UserSession storeUserSession(UserDto userDto) throws Exception;
    String generateCookie(String name, String value) throws Exception;
    UserSession getUserSessionByToken(String token) throws Exception;
    UserDto getUserDtoByUserSession(UserSession userSession) throws Exception;
    UserDto signOut(String token) throws Exception;
    UserDto getUserInfo(String userId) throws Exception;
    UserDto modifyUser(UserDto userDto) throws Exception;
}
