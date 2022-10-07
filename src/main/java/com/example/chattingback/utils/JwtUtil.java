package com.example.chattingback.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.chattingback.eneity.dbEntities.User;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private static final long EXPIRE_TIME = 1000 * 60 * 60 * 24;

    private static final String SECRET_KEY = "#$%^&*()gfjbjasnl";

    public static String releaseToken(User user) {
        Date expire_at = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create()
                .withClaim("password", user.getPassword())
                .withClaim("userId",user.getUserId())
                .withExpiresAt(expire_at)
                .sign(algorithm);
    }

}
