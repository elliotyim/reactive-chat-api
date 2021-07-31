package com.elldev.reactivechat.service;

import com.elldev.reactivechat.constant.ErrorCode;
import com.elldev.reactivechat.dto.UserDto;
import com.elldev.reactivechat.entity.User;
import com.elldev.reactivechat.entity.UserSession;
import com.elldev.reactivechat.exception.BadRequestException;
import com.elldev.reactivechat.exception.UserSessionNotFoundException;
import com.elldev.reactivechat.repository.UserRepository;
import com.elldev.reactivechat.repository.UserSessionRepository;
import com.elldev.reactivechat.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ApplicationContext ctx;
    private final S3Util s3;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;

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

    private User getUserByUserId(String userId) throws BadRequestException {
        return userRepository.findById(userId).orElseThrow(
                () -> new BadRequestException(ErrorCode.USER_NOT_EXIST, "No such user exists")
        );
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

    @Override
    public UserDto signOut(String token) throws Exception {
        UserSession userSession = getUserSessionByToken(token);
        UserDto userDto = getUserDtoByUserSession(userSession);
        userSessionRepository.delete(userSession);
        return userDto;
    }

    @Override
    public UserDto getUserInfo(String userId) throws BadRequestException {
        User user = getUserByUserId(userId);
        return UserDto.convertToDto(user);
    }

    public String uploadProfileImg(String userId, MultipartFile imgFile) throws IOException {
        String bucketName = ctx.getBean("bucketName", String.class);
        String filePath = "profile/" + userId + "/" + UUID.randomUUID().toString();
        URL savedImgUrl = s3.saveFile(imgFile, bucketName, filePath);
        return savedImgUrl.toExternalForm();
    }

    @Override
    public UserDto modifyUser(UserDto userDto) throws BadRequestException, IOException {
        User user = getUserByUserId(userDto.getId());

        String profileImgUrl = null;
        if (userDto.getProfileImgFile() != null)
            profileImgUrl = uploadProfileImg(user.getId(), userDto.getProfileImgFile());

        boolean isSamePassword = verifyPassword(userDto.getPassword(), user.getPassword());
        if (userDto.getPassword() != null && !userDto.getPassword().trim().equals("") && !isSamePassword)
            user.setPassword(encodePassword(userDto.getPassword()));

        user.setName(userDto.getName() != null ? userDto.getName() : user.getName())
                .setProfileImg(userDto.getProfileImgFile() != null ? profileImgUrl : user.getProfileImg());

        User updatedUser = userRepository.save(user);
        return UserDto.convertToDto(updatedUser);
    }

}
