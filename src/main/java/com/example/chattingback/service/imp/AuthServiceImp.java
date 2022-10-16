package com.example.chattingback.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.chattingback.controller.socketIO.ServerRunner;
import com.example.chattingback.eneity.dbEntities.User;
import com.example.chattingback.mapper.UserMapper;
import com.example.chattingback.service.AuthService;
import com.example.chattingback.utils.MyBeanUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthServiceImp implements AuthService {
    @Resource
    private ServerRunner serverRunner;
    @Resource
    private UserMapper userMapper;

    @Resource
    private MyBeanUtils<Object, User> myBeanUtils;

    @Resource
    private com.example.chattingback.utils.securityUtil securityUtil;

    @Override
    public User checkUserFromDb(User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername());
        Object result = userMapper.selectOne(wrapper);
        User resUser = myBeanUtils.copyProperties(result, User.class);
        return resUser;
    }

    @Override
    public boolean isUsernameUsed(User user) {
        User userFromDb = checkUserFromDb(user);
        if (userFromDb == null){
            return false;
        }
        if(userFromDb.getUsername().isEmpty() || !userFromDb.getUsername().equals(user.getUsername())){
            return false;
        }
        return true;
    }

    @Override
    public boolean insertIntoUserDb(User user) {
        int insertResult = userMapper.insert(user);
        if (insertResult == 1){
            return true;
        }
        return false;
    }

    @Override
    public User newUserConstrator(User user) {
        User newUser = new User();
        newUser.setPassword(securityUtil.bCryptEncodePassword(user.getPassword()));
        newUser.setUsername(user.getUsername());
        newUser.setRole("user");
        newUser.setCreateTime(new Date().getTime());
        newUser.setAvator("api/avatar/avatar("+ (int)(Math.random()*19 + 1) +").png ");
        newUser.setUserId(UUID.randomUUID().toString());
        return newUser;
    }

    @Override
    public boolean matchUser(User user) {
        User userFromDb = checkUserFromDb(user);
        boolean compareResult = passwordCompare(user, userFromDb);
        if (BooleanUtils.isTrue(compareResult)){
            return true;
        }
        return false;
    }

    @Override
    public boolean passwordCompare(User user, User userFromDb) {
        Boolean matchPassword = securityUtil.bCryptMatchPassword(user.getPassword(), userFromDb.getPassword());
        if (BooleanUtils.isTrue(matchPassword)){
            return true;
        }
        return false;
    }


}
