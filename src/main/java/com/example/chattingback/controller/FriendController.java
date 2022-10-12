package com.example.chattingback.controller;

import com.example.chattingback.eneity.response.Response;
import com.example.chattingback.eneity.response.pagingParams;
import com.example.chattingback.service.imp.FriendServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("friend")
public class FriendController {

    @Autowired
    private FriendServiceImp friendServiceImp;
//
//    @GetMapping("/findByName")
//    public Response getFriendByUserName(@RequestParam String username){
//        Response response = friendServiceImp.findByName(username);
//        return response;
//    }

    @GetMapping("/")
    public Response getFriends(@PathVariable String userId){
        Response response = friendServiceImp.getUserFriends(userId);
        return response;
    }

    @GetMapping("/friendMessages")
    public  Response getFriendMessage(pagingParams data){
        Response response = friendServiceImp.getFriendMessages(data);
        if (1 == 1) {
            System.out.println("=========================================================");
            System.out.println(response.getData().toString());
        }
        return response;
    }
}
