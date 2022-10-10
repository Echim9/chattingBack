package com.example.chattingback.controller;

import com.example.chattingback.service.imp.AvatarServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author Echim9
 * @date 2022/10/10 15:31
 */

@RestController
public class AvatarController {

    @Autowired
    private AvatarServiceImp avatarService;
    @GetMapping(value = "/avatar/{avatarId}",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getAvatar(@PathVariable String avatarId) throws IOException {
        byte[] bytes = avatarService.returnAvatar(avatarId);
        System.out.println(bytes);
        return bytes;
    }
}
