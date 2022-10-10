package com.example.chattingback.eneity;

import com.example.chattingback.eneity.chat.FriendDto;
import com.example.chattingback.eneity.chat.GroupDto;
import com.example.chattingback.eneity.dbEntities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class payload {
    private ArrayList<GroupDto> groupData;

    private ArrayList<FriendDto> friendData;

    private HashMap<String, User> userData;
}
