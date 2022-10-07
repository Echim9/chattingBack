package com.example.chattingback.eneity.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {

    private String groupId;

    private String userId;// 群主id

    private String groupName;

    private String notice;

    private GroupMessageDto GroupMessageDto[];

    private int createTime;
}
