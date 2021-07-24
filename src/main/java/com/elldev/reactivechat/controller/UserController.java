package com.elldev.reactivechat.controller;

import com.elldev.reactivechat.dto.UserDto;
import com.elldev.reactivechat.entity.UserSession;
import com.elldev.reactivechat.exception.UserSessionNotFoundException;
import com.elldev.reactivechat.service.UserService;
import com.elldev.reactivechat.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDto> checkIfSignedIn(
            @CookieValue(value = "token") String token
    ) throws Exception {
        UserSession userSession = userService.getUserSessionByToken(token);
        UserDto userDto = userService.getUserDtoByUserSession(userSession);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping
    public ResponseEntity<UserDto> signIn(
            @RequestBody UserDto userDto,
            HttpServletResponse response
    ) throws Exception {
        UserValidator.checkSignInUserInput(userDto);

        UserDto signedInUser = userService.signIn(userDto);
        UserSession userSession = userService.storeUserSession(userDto);
        String cookie = userService.generateCookie("token", userSession.getToken());

        response.addHeader(HttpHeaders.SET_COOKIE, cookie);
        return ResponseEntity.ok(signedInUser);
    }

    @PostMapping
    public ResponseEntity<UserDto> signUp(@RequestBody UserDto userDto) throws Exception {
        UserValidator.checkRegistrationUserInput(userDto);
        UserDto registeredUser = userService.signUp(userDto);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(registeredUser);
    }
}
