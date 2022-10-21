package com.example.chattingback.service;

import com.example.chattingback.eneity.dbEntities.User;
import com.example.chattingback.eneity.response.Response;

public interface UserService {

    public Response postUsers(String userIds);

    public Response updateUsername(User user);

    public Response updatePassword(User user, String password);
}
