package com.example.chattingback.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Echim9
 * @date 2022/10/10 15:31
 */

public interface AvatarService {
    public String setAvatar(MultipartFile file) throws IOException;
    }

