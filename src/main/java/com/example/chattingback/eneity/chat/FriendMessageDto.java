package com.example.chattingback.eneity.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendMessageDto {

    private String friendId;

    private String userId;

    private String content;

    private String width;

    private String height;

    private String messageType;

    private int time;
}
