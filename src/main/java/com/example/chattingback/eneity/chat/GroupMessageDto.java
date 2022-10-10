package com.example.chattingback.eneity.chat;

import com.example.chattingback.eneity.dbEntities.GroupMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMessageDto {

    private String groupId;

    private String userId;

    private String content;

    private String width;

    private String height;

    private String messageType;

    private long time;

    public static GroupMessage initializeGroupMessageDto(GroupMessageDto groupMessageDto){
        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setGroupId(groupMessageDto.getGroupId());
        groupMessage.setMessageType(groupMessageDto.getMessageType());
        groupMessage.setTime(groupMessageDto.getTime());
        groupMessage.setUserId(groupMessageDto.getUserId());
        groupMessage.setContent(groupMessageDto.getContent());
        return groupMessage;
    }
}
