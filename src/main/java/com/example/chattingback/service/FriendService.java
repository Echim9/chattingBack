package com.example.chattingback.service;


import com.example.chattingback.eneity.response.Response;
import com.example.chattingback.eneity.response.pagingParams;

public interface FriendService {

    public Response findByName(String userName);

    public Response getUserFriends(String userId);

    public Response getFriendMessages(pagingParams data);
}
