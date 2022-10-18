package com.example.chattingback.eneity.chat;

import com.example.chattingback.eneity.dbEntities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Echim9
 * @date 2022/10/18 14:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendDtoNull {
    private String username;

    private String userId;

    private String avatar;

    private String role;

    private String tag;

    private long createTime;

    public static FriendDto initializeFriendDto(User user){
        FriendDto friendDto = new FriendDto();
        friendDto.setAvatar(user.getAvatar());
        friendDto.setRole(user.getRole());
        friendDto.setTag(user.getTag());
        friendDto.setRole(user.getRole());
        friendDto.setUsername(user.getUsername());
        friendDto.setUserId(user.getUserId());
        friendDto.setCreateTime(user.getCreateTime());
        return friendDto;
    }
}
