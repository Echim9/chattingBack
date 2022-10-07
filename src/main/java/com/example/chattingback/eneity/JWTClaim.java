package com.example.chattingback.eneity;

import lombok.Data;

@Data
public class JWTClaim {

    private long expireTime;

    private String userId;

    private String password;
}
