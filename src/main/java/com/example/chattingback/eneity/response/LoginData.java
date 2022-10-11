package com.example.chattingback.eneity.response;

import com.example.chattingback.eneity.dbEntities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginData {

    private User user;

    private String token;
}
