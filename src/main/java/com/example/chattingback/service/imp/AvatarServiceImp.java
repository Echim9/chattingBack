package com.example.chattingback.service.imp;

import com.example.chattingback.service.AvatarService;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Echim9
 * @date 2022/10/10 15:32
 */
@Service
public class AvatarServiceImp implements AvatarService {
    String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
    String filePath = path + "static/avatar/";

    @Override
    public byte[] returnAvatar(String avatar) throws IOException {
        File file = new File(filePath + avatar);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;

    }
}
