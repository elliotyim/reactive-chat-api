package com.elldev.reactivechat.controller;

import com.elldev.reactivechat.dto.UserDto;
import com.elldev.reactivechat.entity.UserSession;
import com.elldev.reactivechat.service.UserService;
import com.elldev.reactivechat.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/signin-check")
    public ResponseEntity<UserDto> checkIfSignedIn(
            @CookieValue(value = "token", required = false) String token
    ) throws Exception {
        UserSession userSession = userService.getUserSessionByToken(token);
        UserDto userDto = userService.getUserDtoByUserSession(userSession);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/signin")
    public ResponseEntity<UserDto> signIn(
            @RequestBody UserDto userDto,
            HttpServletResponse response
    ) throws Exception {
        UserValidator.checkSignInUserInput(userDto);

        UserDto signedInUser = userService.signIn(userDto);
        UserSession userSession = userService.storeUserSession(userDto);

        String cookie = userService.generateCookie("token", userSession.getToken());
        response.addHeader(HttpHeaders.SET_COOKIE, cookie);

        log.info("User " + signedInUser.getName() + " is signed in.");
        return ResponseEntity.ok(signedInUser);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody UserDto userDto) throws Exception {
        UserValidator.checkRegistrationUserInput(userDto);
        UserDto registeredUser = userService.signUp(userDto);

        log.info("User " + registeredUser.getName() + " is registered.");
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(registeredUser);
    }

    @PostMapping("/signout")
    public ResponseEntity signOut(
            @CookieValue(value = "token") String token
    ) throws Exception {
        UserDto signedOutUser = userService.signOut(token);
        log.info("User " + signedOutUser.getName() + " is signed out.");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
