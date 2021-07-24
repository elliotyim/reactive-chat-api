package com.elldev.reactivechat.service;

import com.elldev.reactivechat.repository.UserRepository;
import com.elldev.reactivechat.repository.UserSessionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;

class UserServiceImplTest {

    UserServiceImpl userService;

    public UserServiceImplTest() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UserRepository userRepository = mock(UserRepository.class);
        UserSessionRepository userSessionRepository = mock(UserSessionRepository.class);
        ApplicationContext ctx = mock(ApplicationContext.class);
        this.userService = new UserServiceImpl(encoder, userRepository, userSessionRepository, ctx);
    }

    @Test
    void Is_Password_EncodedSafely() {
        // given
        String rawPassword = "testPassword123";

        // when
        String encodePassword1 = userService.encodePassword(rawPassword);
        String encodePassword2 = userService.encodePassword(rawPassword);

        // then
        assertNotEquals(encodePassword1, encodePassword2);
    }

    @Test
    void Is_Password_VerifiedProperly() {
        // given
        String rawPassword = "testPassword123";

        // when
        String encodePassword1 = userService.encodePassword(rawPassword);
        String encodePassword2 = userService.encodePassword(rawPassword);

        // then
        boolean isPassword1Valid = userService.verifyPassword(rawPassword, encodePassword1);
        assertEquals(isPassword1Valid, true);
        boolean isPassword2Valid = userService.verifyPassword(rawPassword, encodePassword2);
        assertEquals(isPassword2Valid, true );
    }
}