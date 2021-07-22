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

    public static void checkRegistrationUserInput(UserDto userDto) throws BadRequestException {
        if (userDto.getEmail() == null || !validateEmail(userDto.getEmail()) || userDto.getEmail().length() > 50)
            throw new BadRequestException(ErrorCode.INVALID_EMAIL_PROVIDED, "Check the email");
        else if (userDto.getName() == null || userDto.getName().length() > 30)
            throw new BadRequestException(ErrorCode.INVALID_NAME_PROVIDED, "Check the name");
        else if (userDto.getPassword() == null)
            throw new BadRequestException(ErrorCode.PASSWORD_NOT_PROVIDED, "Check the password");
    }
}
