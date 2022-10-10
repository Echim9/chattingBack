package com.example.chattingback.eneity;

import com.example.chattingback.eneity.dbEntities.Group;
import com.example.chattingback.eneity.dbEntities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinRes {

    Group group;

    User user;
}
