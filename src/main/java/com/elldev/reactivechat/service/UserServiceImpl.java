package com.elldev.reactivechat.service;

import com.elldev.reactivechat.constant.ErrorCode;
import com.elldev.reactivechat.dto.UserDto;
import com.elldev.reactivechat.entity.User;
import com.elldev.reactivechat.entity.UserSession;
import com.elldev.reactivechat.exception.BadRequestException;
import com.elldev.reactivechat.exception.UserSessionNotFoundException;
import com.elldev.reactivechat.repository.UserRepository;
import com.elldev.reactivechat.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    private final ApplicationContext ctx;

    public void checkIfEmailAlreadyRegistered(String email) throws BadRequestException {
        if (userRepository.existsByEmail(email))
            throw new BadRequestException(ErrorCode.USER_ALREADY_EXIST, "User already registered!");
    }

    public String encodePassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public UserDto signIn(UserDto userDto) throws Exception {
        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(
                () -> new BadRequestException(ErrorCode.USER_NOT_REGISTERED, "Check the email or password")
        );

        boolean isPasswordValid = verifyPassword(userDto.getPassword(), user.getPassword());
        if (!isPasswordValid)
            throw new BadRequestException(ErrorCode.INVALID_PASSWORD_PROVIDED, "Check the email or password");

        return UserDto.convertToDto(user);
    }

    @Override
    public UserDto signUp(UserDto userDto) throws BadRequestException {
        checkIfEmailAlreadyRegistered(userDto.getEmail());

        userDto.setPassword(encodePassword(userDto.getPassword()));
        User user = userRepository.save(UserDto.convertToEntity(userDto));

        return UserDto.convertToDto(user);
    }

    @Override
    public UserSession storeUserSession(UserDto userDto) {
        String token = UUID.randomUUID().toString();
        UserSession userSession = userSessionRepository.save(UserSession.builder()
                .token(token)
                .userId(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail()).build());
        return userSession;
    }

    @Override
    public String generateCookie(String name, String value) {
        Boolean httpSecure = ctx.getBean("isHttpSecure", Boolean.class);
        return ResponseCookie.from(name, value)
                .sameSite("LAX")
                .path("/")
                .httpOnly(true)
                .secure(httpSecure)
                .build().toString();
    }

    @Override
    public UserSession getUserSessionByToken(String token) throws UserSessionNotFoundException {
        if (token == null)
            throw new UserSessionNotFoundException(ErrorCode.USER_SESSION_NOT_FOUND, "User not singed in!");

        return userSessionRepository.findByToken(token).orElseThrow(
                () -> new UserSessionNotFoundException(ErrorCode.USER_SESSION_NOT_FOUND, "User not singed in!")
        );
    }

    @Override
    public UserDto getUserDtoByUserSession(UserSession userSession) throws Exception {
        User user = userRepository.findByEmail(userSession.getEmail()).orElseThrow(
                () -> new BadRequestException(ErrorCode.USER_NOT_EXIST, "User not exists")
        );
        return UserDto.convertToDto(user);
    }

}
