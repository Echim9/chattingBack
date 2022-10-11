package com.example.chattingback.controller;

import com.example.chattingback.eneity.response.Response;
import com.example.chattingback.service.imp.FriendServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class FriendController {

    @Autowired
    private FriendServiceImp friendServiceImp;

    @GetMapping("/findByName")
    public Response getFriendByUserName(@RequestParam String username){
        Response response = friendServiceImp.findByName(username);
        return response;
    }

    public Response getFriends(String userId){
        Response response = friendServiceImp.getUserFriends(userId);
        return response;
    }
}
