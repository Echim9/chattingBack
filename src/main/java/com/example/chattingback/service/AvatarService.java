package com.example.chattingback.service;

import java.io.IOException;

/**
 * @author Echim9
 * @date 2022/10/10 15:31
 */

public interface AvatarService {
    public byte[] returnAvatar(String avatar) throws IOException;
    }

