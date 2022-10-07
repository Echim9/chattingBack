package com.example.chattingback.service;

import com.example.chattingback.eneity.dbEntities.User;

public interface AuthService {
    public Object checkUserFromDb(User user);

    public boolean isUsernameUsed(User user);

    public boolean insertIntoUserDb(User user);

    public User newUserConstrator(User user);

    public boolean matchUser(User user);

    public boolean passwordCompare(User userFromDb, User user);
}
