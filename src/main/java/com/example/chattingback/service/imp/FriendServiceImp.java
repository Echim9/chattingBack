package com.example.chattingback.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.chattingback.eneity.dbEntities.FriendMessage;
import com.example.chattingback.eneity.dbEntities.User;
import com.example.chattingback.eneity.dbEntities.UserFriend;
import com.example.chattingback.eneity.response.Response;
import com.example.chattingback.eneity.response.pagingParams;
import com.example.chattingback.enums.Rcode;
import com.example.chattingback.mapper.FriendMessageMapper;
import com.example.chattingback.mapper.UserFriendMapper;
import com.example.chattingback.mapper.UserMapper;
import com.example.chattingback.service.FriendService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FriendServiceImp implements FriendService {

    @Autowired
    private FriendMessageMapper friendMessageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserFriendMapper userFriendMapper;

    @Override
    public Response findByName(String username) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper
                .like("username", username)
                .isNotNull("username");
        List<User> users = userMapper.selectList(userQueryWrapper);
        if (ObjectUtils.isEmpty(users)) {
            return Response
                    .builder()
                    .code(Rcode.FAIL)
                    .msg("请输入用户名")
                    .data(null)
                    .build();
        } else if (!ObjectUtils.isEmpty(users)) {
            System.out.println("成功查找用户" + users);
            return Response
                    .builder()
                    .data(users)
                    .build();
        }
        return Response
                .builder()
                .code(Rcode.ERROR)
                .msg("查询错误，请联系管理员解决")
                .data(null)
                .build();
    }

    @Override
    public Response getUserFriends(String userId) {
        QueryWrapper<UserFriend> userFriendQueryWrapper = new QueryWrapper<>();
        userFriendQueryWrapper
                .isNotNull(userId)
                .eq("userId", userId);
        List<UserFriend> userFriends = userFriendMapper.selectList(userFriendQueryWrapper);
        if (ObjectUtils.isEmpty(userFriends)) {
            return Response
                    .builder()
                    .code(Rcode.FAIL)
                    .msg("好友拉取失败或无好友")
                    .data(null)
                    .build();
        } else if (!ObjectUtils.isEmpty(userFriends)) {
            return Response
                    .builder()
                    .msg("好友拉取成功")
                    .data(userFriends)
                    .build();
        }
        return Response
                .builder()
                .code(Rcode.ERROR)
                .msg("好友拉取错误，请联系管理员解决")
                .data(null)
                .build();
    }

    //有大问题！之后需要修改，逻辑是错误的
    @Override
    public Response getFriendMessages(pagingParams data) {
        Page<FriendMessage> page = new Page<>((data.getCurrent() / data.getPageSize()) + 2, data.getPageSize());
        QueryWrapper<FriendMessage> friendMessageQueryWrapper1 = new QueryWrapper<>();
        friendMessageQueryWrapper1.eq("userId", data.getUserId()).eq("friendId", data.getFriendId()).orderByDesc("time");
        QueryWrapper<FriendMessage> friendMessageQueryWrapper2 = new QueryWrapper<>();
        friendMessageQueryWrapper2.eq("userId", data.getFriendId()).eq("friendId", data.getUserId()).orderByDesc("time");
        Page<FriendMessage> friendMessages1 = friendMessageMapper.selectPage(page, friendMessageQueryWrapper1);
        Page<FriendMessage> friendMessages2 = friendMessageMapper.selectPage(page, friendMessageQueryWrapper2);
        List<FriendMessage> records1 = friendMessages1.getRecords();
        List<FriendMessage> records2 = friendMessages2.getRecords();
        ArrayList<FriendMessage> friendMessageArr = new ArrayList<>();
        for (int i = data.getCurrent(); i < data.getCurrent() + data.getPageSize() - 1; i++) {
            if (ObjectUtils.isNotEmpty(records1.get(i))) {
                friendMessageArr.add(records1.get(i));
            }
        }for (int i = data.getCurrent(); i < data.getCurrent() + data.getPageSize() - 1; i++) {
            if (ObjectUtils.isNotEmpty(records2.get(i))) {
                friendMessageArr.add(records2.get(i));
            }
        }
        return new Response()
                .builder()
                .data(friendMessageArr)
                .msg("")
                .build();
    }
}
