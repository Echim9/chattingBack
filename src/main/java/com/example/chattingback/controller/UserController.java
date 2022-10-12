package com.example.chattingback.controller;

import com.example.chattingback.eneity.response.Response;
import com.example.chattingback.service.imp.FriendServiceImp;
import com.example.chattingback.service.imp.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private FriendServiceImp friendServiceImp;

    @Autowired
    private UserServiceImp userServiceImp;



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
}
