package com.elldev.reactivechat.controller;

import com.elldev.reactivechat.dto.UserDto;
import com.elldev.reactivechat.service.UserService;
import com.elldev.reactivechat.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> signUp(@RequestBody UserDto userDto) throws Exception {
        UserValidator.checkRegistrationUserInput(userDto);
        UserDto registeredUser = userService.register(userDto);
        return ResponseEntity.ok(registeredUser);
    }
}
