package com.elldev.reactivechat.service;

import com.elldev.reactivechat.constant.ErrorCode;
import com.elldev.reactivechat.dto.UserDto;
import com.elldev.reactivechat.entity.User;
import com.elldev.reactivechat.exception.BadRequestException;
import com.elldev.reactivechat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

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
    public UserDto register(UserDto userDto) throws BadRequestException {
        checkIfEmailAlreadyRegistered(userDto.getEmail());

        userDto.setPassword(encodePassword(userDto.getPassword()));
        User user = userRepository.save(UserDto.convertToEntity(userDto));

        return UserDto.convertToDto(user);
    }
}
