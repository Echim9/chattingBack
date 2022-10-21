package com.example.chattingback.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.chattingback.eneity.dbEntities.FriendMessage;
import com.example.chattingback.eneity.dbEntities.User;
import com.example.chattingback.eneity.dbEntities.UserFriend;
import com.example.chattingback.eneity.response.GroupMesRes;
import com.example.chattingback.eneity.response.Response;
import com.example.chattingback.eneity.response.pagingParams;
import com.example.chattingback.enums.Rcode;
import com.example.chattingback.Resource.mapper.FriendMessageMapper;
import com.example.chattingback.Resource.mapper.UserFriendMapper;
import com.example.chattingback.Resource.mapper.UserMapper;
import com.example.chattingback.service.FriendService;
import com.example.chattingback.utils.RedisUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.chattingback.enums.RedisCache.CACHE_USER_FRIENDS;
import static com.example.chattingback.enums.RedisCache.EXPIRE_TIME;

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
        //先从redis里尝试取出
        String userFriends = RedisUtil.get(CACHE_USER_FRIENDS + userId);
        if (StringUtils.isEmpty(userFriends)) {
            QueryWrapper<UserFriend> userFriendQueryWrapper = new QueryWrapper<>();
            userFriendQueryWrapper
                    .isNotNull(userId)
                    .eq("userId", userId);
            List<UserFriend> userFriendArr = userFriendMapper.selectList(userFriendQueryWrapper);
            if (ObjectUtils.isEmpty(userFriendArr)) {
                return Response
                        .builder()
                        .code(Rcode.FAIL)
                        .msg("好友拉取失败或无好友")
                        .data(null)
                        .build();
            } else if (!ObjectUtils.isEmpty(userFriendArr)) {
                //存入redis
                String userFriendArrJson = JSONObject.toJSONString(userFriendArr);
                RedisUtil.set(CACHE_USER_FRIENDS + userId, userFriendArrJson,EXPIRE_TIME);
                return Response
                        .builder()
                        .msg("好友拉取成功")
                        .data(userFriendArr)
                        .build();
            }
            return Response
                    .builder()
                    .code(Rcode.ERROR)
                    .msg("好友拉取错误，请联系管理员解决")
                    .data(null)
                    .build();
        } else {
            String resultFromRedis = RedisUtil.get(CACHE_USER_FRIENDS + userId);
            ArrayList friendsArr = JSONObject.toJavaObject(JSONObject.parseObject(resultFromRedis), ArrayList.class);
            return Response
                    .builder()
                    .msg("好友拉取成功")
                    .data(friendsArr)
                    .build();
        }
    }


    @Override
    public Response getFriendMessages(pagingParams data) {
        ArrayList<FriendMessage> friendMessages = friendMessageMapper.selectFriendMessagesBySqlPage(data.getUserId(), data.getFriendId(), data.getCurrent(), data.getPageSize());
        GroupMesRes groupMesRes = new GroupMesRes();
        groupMesRes.setMessageArr(friendMessages);
        for (FriendMessage message : friendMessages) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userId", message.getUserId());
            User user = userMapper.selectOne(queryWrapper);
            if (ObjectUtils.isEmpty(user)) {
                groupMesRes.getUserArr().add(user);
            }
        }
        if (ObjectUtils.isEmpty(friendMessages)){
            return new Response()
                    .builder()
                    .data("")
                    .msg("已无更多信息")
                    .build();
        }
        return new Response()
                .builder()
                .data(groupMesRes)
                .msg("")
                .build();
    }
}
