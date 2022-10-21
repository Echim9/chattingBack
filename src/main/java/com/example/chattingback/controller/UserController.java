package com.example.chattingback.controller;

import com.example.chattingback.eneity.dbEntities.User;
import com.example.chattingback.eneity.response.Response;
import com.example.chattingback.enums.Rcode;
import com.example.chattingback.service.imp.AvatarServiceImp;
import com.example.chattingback.service.imp.FriendServiceImp;
import com.example.chattingback.service.imp.UserServiceImp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("user")
public class UserController {
    @Resource
    private FriendServiceImp friendServiceImp;

    @Resource
    private UserServiceImp userServiceImp;

    @Resource
    private AvatarServiceImp avatarServiceImp;


    @GetMapping("/findByName")
    public Response getFriendByUserName(@RequestParam String username){
        Response response = friendServiceImp.findByName(username);
        return response;
    }

    @PostMapping("")
    public Response postGroups(@RequestBody String userIds){
        Response response = userServiceImp.postUsers(userIds);
        return response;
    }

    @PatchMapping("username")
    public Response updateUsername(@RequestBody User user){
        Response response = userServiceImp.updateUsername(user);
        return response;
    }

    @PatchMapping("password")
    public Response updatePassword(@RequestBody User user, @RequestParam("password") String password) {
        Response response = userServiceImp.updatePassword(user, password);
        return response;
    }

    @PostMapping ("avatar")
    public Response updateAvatar( User user, @RequestPart("file") MultipartFile file) throws IOException {

        String avatar = avatarServiceImp.setAvatar(file);
        if (StringUtils.isEmpty(avatar)){
            return Response
                    .builder()
                    .msg("更新头像失败")
                    .data("")
                    .code(Rcode.FAIL)
                    .build();
        }
        else {
            user.setAvatar(avatar);
            return Response
                    .builder()
                    .msg("头像更新成功")
                    .build();
        }
    }
}
