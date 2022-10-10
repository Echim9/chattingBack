package com.example.chattingback.eneity.chat;

import com.example.chattingback.eneity.dbEntities.FriendMessage;
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

    private long time;
    public static FriendMessage initializeFriendMessageDto(FriendMessageDto friendMessageDto){
        FriendMessage friendMessage = new FriendMessage();
        friendMessage.setFriendId(friendMessageDto.getFriendId());
        friendMessage.setContent(friendMessageDto.getContent());
        friendMessage.setTime(friendMessageDto.getTime());
        friendMessage.setUserId(friendMessageDto.getUserId());
        friendMessage.setMessageType(friendMessageDto.getMessageType());
        return friendMessage;
    }
}
