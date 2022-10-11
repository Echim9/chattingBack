package com.example.chattingback.eneity;

import com.example.chattingback.eneity.chat.FriendDto;
import com.example.chattingback.eneity.chat.GroupDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class payload {
    private ArrayList<GroupDto> groupData;

    private ArrayList<FriendDto> friendData;

    private ArrayList<FriendDto> userData;
}
