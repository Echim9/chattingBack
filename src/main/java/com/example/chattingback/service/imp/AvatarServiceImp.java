package com.example.chattingback.service.imp;

import com.example.chattingback.service.AvatarService;
import com.example.chattingback.utils.imageUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Echim9
 * @date 2022/10/10 15:32
 */
@Service
public class AvatarServiceImp implements AvatarService {
    String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
    String filePath = path + "static/avatar/";

    String avatarUrl = "api/avatar/";

    @Override
    public String setAvatar(MultipartFile multipartFile) throws IOException {
        File file = imageUtils.transferToFile(multipartFile);
        BufferedImage image = ImageIO.read(file);
        String imageName = imageUtils.createImageName();
        String name = file.getName();
        String newName = imageName + name;
        boolean writeImage = imageUtils.writeImage(image, newName, filePath);
        if (Boolean.TRUE.equals(writeImage)){
            return avatarUrl + filePath;
        }
        else {
            return "";
        }
    }
}
