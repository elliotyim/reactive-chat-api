package com.elldev.reactivechat.dto;

import com.elldev.reactivechat.entity.Friendship;
import com.elldev.reactivechat.entity.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private String id;
    private String name;
    private String email;
    private String password;
    private String profileImg;
    private MultipartFile profileImgFile;
    private LocalDateTime createdAt;

    private List<UserDto> friends;

    public static User convertToEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .profileImg(userDto.getProfileImg()).build();
    }

    private static UserDto buildUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profileImg(user.getProfileImg())
                .createdAt(user.getCreatedAt())
                .build();
    }

    private static List<UserDto> getMyFriends(String myId, Set<Friendship> ...friendshipsList) {
        List<UserDto> myFriends = new ArrayList<>();
        for (Set<Friendship> friendships : friendshipsList) {
            for (Friendship friendship : friendships) {
                User friendOne = friendship.getFriendOne();
                User friendTwo = friendship.getFriendTwo();

                if (friendOne.getId().equals(myId)) {
                    myFriends.add(buildUserDto(friendTwo));
                } else if (friendTwo.getId().equals(myId)) {
                    myFriends.add(buildUserDto(friendOne));
                }
            }
        }
        return myFriends;
    }

    public static UserDto convertToDto(User user) {
        List<UserDto> myFriends = getMyFriends(user.getId(), user.getFriendshipsOne(), user.getFriendshipsTwo());

        UserDto userDto = buildUserDto(user);
        userDto.setFriends(myFriends);
        return userDto;
    }

    public static List<UserDto> convertToDtoList(List<User> users) {
        return users.stream().map(u -> convertToDto(u)).collect(Collectors.toList());
    }
}
