package com.example.chattingback.service;


import com.example.chattingback.eneity.Response;

public interface FriendService {

    public Response findByName(String userName);

    public Response getUserFriends(String userId);
}
