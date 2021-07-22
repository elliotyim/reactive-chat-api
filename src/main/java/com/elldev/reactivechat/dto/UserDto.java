package com.elldev.reactivechat.dto;

import com.elldev.reactivechat.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String profileImg;
    private LocalDateTime createdAt;

    public static User convertToEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .profileImg(userDto.getProfileImg()).build();
    }

    public static UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profileImg(user.getProfileImg())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static List<UserDto> convertToDtoList(List<User> users) {
        return users.stream().map(u -> convertToDto(u)).collect(Collectors.toList());
    }
}
