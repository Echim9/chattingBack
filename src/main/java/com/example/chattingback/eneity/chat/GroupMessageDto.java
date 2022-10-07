package com.example.chattingback.eneity.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMessageDto {

    private String groupId;

    private String userId;// 群主id

    private String content;

    private String width;

    private String height;

    private String messageType;

    private int time;
}
