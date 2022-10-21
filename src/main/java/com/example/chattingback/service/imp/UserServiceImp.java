package com.example.chattingback.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.chattingback.bean.redissonBean;
import com.example.chattingback.controller.socketIO.ServerRunner;
import com.example.chattingback.eneity.dbEntities.User;
import com.example.chattingback.eneity.response.Response;
import com.example.chattingback.enums.Rcode;
import com.example.chattingback.Resource.mapper.UserMapper;
import com.example.chattingback.service.UserService;
import com.example.chattingback.utils.VerifyUtil;
import com.example.chattingback.utils.securityUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.redisson.api.RBloomFilter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

@Service
public class UserServiceImp implements UserService {

    @Resource
    private ServerRunner serverRunner;
    @Resource
    private UserMapper usermapper;

    @Override
    public Response postUsers(String userIds) {
        ArrayList<User> users = new ArrayList<>();
        try {
            if (userIds.length() > 0) {
                String[] id = userIds.split(",");
                for (String userId : id) {
                    RBloomFilter userBloomFilter = redissonBean.userBloomFilter;
                    if (userBloomFilter.contains(Boolean.TRUE.equals(userId))) {
                        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                        userQueryWrapper.eq("userId", userId);
                        User user = usermapper.selectOne(userQueryWrapper);
                        if (ObjectUtils.isNotEmpty(user)) {
                            users.add(user);
                        }
                    }
                }
                return new Response()
                        .builder()
                        .msg("获取用户信息成功")
                        .data(users)
                        .build();
            }
            return new Response()
                    .builder()
                    .msg("获取用户信息失败")
                    .code(Rcode.FAIL)
                    .data("")
                    .build();
        } catch (Exception e) {
            return new Response()
                    .builder()
                    .msg("获取用户失败")
                    .code(Rcode.ERROR)
                    .data("")
                    .build();
        }
    }

    @Override
    public Response updateUsername(User user) {
        try {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userId", user.getUserId());
            String newUsername = user.getUsername();
            //判断username是否重复
            QueryWrapper<User> isSameName = new QueryWrapper<User>().eq("username", newUsername);
            User selectOne = usermapper.selectOne(isSameName);
            if (ObjectUtils.isNotEmpty(selectOne)) {
                return new Response()
                        .builder()
                        .data("")
                        .code(Rcode.FAIL)
                        .msg("用户名重复")
                        .build();
            }
            Boolean validUsername = VerifyUtil.isValidUsername(newUsername);
            if (Boolean.FALSE.equals(validUsername)) {
                return new Response()
                        .builder()
                        .code(Rcode.FAIL)
                        .msg("用户名格式要求3～10位英文数字")
                        .build();
            }
            usermapper.update(user, queryWrapper);
            return new Response()
                    .builder()
                    .msg("用户名修改成功")
                    .data(user)
                    .build();
        } catch (Exception e) {
            return new Response()
                    .builder()
                    .code(Rcode.ERROR)
                    .msg("用户名更新错误")
                    .data(e)
                    .build();
        }
    }

    @Override
    public Response updatePassword(User user, String password) {
        try {
            User newUser;
            newUser = user;
            Boolean aBoolean = securityUtil.bCryptMatchPassword(password, user.getPassword());
            if (Boolean.TRUE.equals(aBoolean)) {
                return new Response()
                        .builder()
                        .code(Rcode.FAIL)
                        .data("")
                        .msg("密码与历史密码相同，修改错误")
                        .build();
            }
            String encodedPassword = securityUtil.bCryptEncodePassword(password);
            newUser.setPassword(encodedPassword);
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("userId", newUser.getUserId());
            int update = usermapper.update(newUser, userQueryWrapper);
            if (!Integer.valueOf(update).equals(0)) {
                return new Response()
                        .builder()
                        .msg("密码修改成功")
                        .data("")
                        .build();
            }
            return new Response()
                    .builder()
                    .code(Rcode.FAIL)
                    .msg("密码修改失败")
                    .data("")
                    .build();
        } catch (Exception e) {
            return new Response()
                    .builder()
                    .msg("密码修改错误")
                    .data("")
                    .code(Rcode.ERROR)
                    .build();
        }

    }
}