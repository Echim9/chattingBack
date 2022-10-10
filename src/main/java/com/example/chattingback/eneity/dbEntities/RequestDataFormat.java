package com.example.chattingback.eneity.dbEntities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDataFormat {

    Group group;

    User user;

    GroupMessage groupMessage;

    UserGroup userGroup;

    UserFriend userFriend;

    FriendMessage friendMessage;

}
