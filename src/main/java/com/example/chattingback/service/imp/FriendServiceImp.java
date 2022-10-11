package com.example.chattingback.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.chattingback.eneity.response.Response;
import com.example.chattingback.eneity.dbEntities.User;
import com.example.chattingback.eneity.dbEntities.UserFriend;
import com.example.chattingback.enums.Rcode;
import com.example.chattingback.mapper.UserFriendMapper;
import com.example.chattingback.mapper.UserMapper;
import com.example.chattingback.service.FriendService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendServiceImp implements FriendService {

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
}
