package com.example.chattingback.service;

import com.example.chattingback.eneity.dbEntities.Group;
import com.example.chattingback.eneity.dbEntities.User;
import com.example.chattingback.eneity.response.Response;

public interface GroupService {

    public boolean AddToGroup(User user, Group group);

    public Response InitGroup(Group group);

    public boolean isGroupAppeared(Group group);

    //用名字来寻找群
    public Response findGroupByName(Group group);

    //获取用户的所有群
    public Response getUserGroups(User user);

    //获取群对所有信息
    public Response getGroupMessages(Group group);

    //获取群的所有用户
    public Response getGroupUsers(Group group);

    public Response postGroups(String groupIds);

    public Response getGroupMessages( String groupId,  int current, int pageSize);


}
