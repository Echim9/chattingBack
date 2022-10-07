package com.example.chattingback.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.chattingback.eneity.Response;
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
                .isNotNull("username")
                .eq("username", username);
        User userSelectedFromDb = userMapper.selectOne(userQueryWrapper);
        if (ObjectUtils.isEmpty(userSelectedFromDb)) {
            return Response
                    .builder()
                    .code(Rcode.FAIL)
                    .msg("查无用户")
                    .data(null)
                    .build();
        } else if (!ObjectUtils.isEmpty(userSelectedFromDb)) {
            return Response
                    .builder()
                    .msg("查询成功")
                    .data(userSelectedFromDb)
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
