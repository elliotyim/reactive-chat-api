package com.elldev.reactivechat.validator;

import com.elldev.reactivechat.dto.UserDto;
import com.elldev.reactivechat.exception.BadRequestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {
    private final Long ID = 1l;
    private final String NAME = "testName";
    private final String EMAIL = "testEmail";
    private final String PASSWORD = "testPassword123";

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .password(PASSWORD).build();
    }

    @Test
    void Should_InputName_Not_Null() {
        // given
        UserDto userDto = this.userDto;

        // when
        userDto.setName(null);

        // then
        assertThrows(
                BadRequestException.class,
                () -> UserValidator.checkRegistrationUserInput(userDto)
        );
    }

    @Test
    void Should_InputEmail_Not_Null() {
        // given
        UserDto userDto = this.userDto;

        // when
        userDto.setEmail(null);

        // then
        assertThrows(
                BadRequestException.class,
                () -> UserValidator.checkRegistrationUserInput(userDto)
        );
    }

    @Test
    void Should_InputEmail_ValidString() {
        // given
        UserDto userDto = this.userDto;

        // when
        String noTopLevelDomain = "wef@fie";
        userDto.setEmail(noTopLevelDomain);

        // then
        assertThrows(
                BadRequestException.class,
                () -> UserValidator.checkRegistrationUserInput(userDto)
        );

        // when
        String invalidTopLevelDomain = "test@test.123";
        userDto.setEmail(invalidTopLevelDomain);

        // then
        assertThrows(
                BadRequestException.class,
                () -> UserValidator.checkRegistrationUserInput(userDto)
        );

        // when
        String noAtMarkEmail = "test.test";
        userDto.setEmail(noAtMarkEmail);

        // then
        assertThrows(
                BadRequestException.class,
                () -> UserValidator.checkRegistrationUserInput(userDto)
        );

        // when
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000000; i++) sb.append("test");
        sb.append("@test.com");

        String tooLongEmail = sb.toString();
        userDto.setEmail(tooLongEmail);

        // then
        assertThrows(
                BadRequestException.class,
                () -> UserValidator.checkRegistrationUserInput(userDto)
        );
    }

    @Test
    void Should_InputPassword_Not_Null() {
        // given
        UserDto userDto = this.userDto;

        // when
        userDto.setPassword(null);

        // then
        assertThrows(
                BadRequestException.class,
                () -> UserValidator.checkRegistrationUserInput(userDto)
        );
    }

    @Test
    void Should_InputPassword_ShortEnough() {
        // given
        UserDto userDto = this.userDto;

        // when
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000000; i++) sb.append("password");

        String tooLongPassword = sb.toString();
        userDto.setPassword(tooLongPassword);

        // then
        assertThrows(
                BadRequestException.class,
                () -> UserValidator.checkRegistrationUserInput(userDto)
        );
    }
}