package com.elldev.reactivechat.validator;

import com.elldev.reactivechat.constant.ErrorCode;
import com.elldev.reactivechat.dto.UserDto;
import com.elldev.reactivechat.exception.BadRequestException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static void checkUserId(String userId) throws BadRequestException {
        if (userId == null || userId.length() < 36)
            throw new BadRequestException(ErrorCode.INVALID_USER_ID_PROVIDED, "Check the userId");
    }

    public static void checkEmail(String email) throws BadRequestException {
        if (email == null || !validateEmail(email) || email.length() > 50)
            throw new BadRequestException(ErrorCode.INVALID_EMAIL_PROVIDED, "Check the email");
    }

    public static void checkName(String name) throws BadRequestException {
        if (name == null || name.length() > 30)
            throw new BadRequestException(ErrorCode.INVALID_NAME_PROVIDED, "Check the name");
    }

    public static void checkPassword(String password) throws BadRequestException {
        if (password == null)
            throw new BadRequestException(ErrorCode.PASSWORD_NOT_PROVIDED, "Check the password");
    }

    public static void checkRegistrationUserInput(UserDto userDto) throws BadRequestException {
        checkEmail(userDto.getEmail());
        checkName(userDto.getName());
        checkPassword(userDto.getPassword());
    }

    public static void checkSignInUserInput(UserDto userDto) throws BadRequestException {
        checkEmail(userDto.getEmail());
        checkPassword(userDto.getPassword());
    }
}
