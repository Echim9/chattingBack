package com.example.chattingback.eneity.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendDto {

    private String username;

    private String userId;

    private String avatar;

    private String role;

    private String tag;

    private GroupMessageDto GroupMessageDto[];

    private int createTime;
}
