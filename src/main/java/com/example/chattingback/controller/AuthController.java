package com.example.chattingback.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.chattingback.eneity.LoginData;
import com.example.chattingback.eneity.Response;
import com.example.chattingback.eneity.dbEntities.User;
import com.example.chattingback.enums.Rcode;
import com.example.chattingback.mapper.UserMapper;
import com.example.chattingback.service.imp.AuthServiceImp;
import com.example.chattingback.service.imp.GroupServiceImp;
import com.example.chattingback.utils.JwtUtil;
import com.example.chattingback.utils.VerifyUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthServiceImp authServiceImp;

    @Autowired
    private GroupServiceImp groupServiceImp;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    public Response register(@RequestBody User user) {
        if (BooleanUtils.isFalse(VerifyUtil.isValidPassword(user.getPassword()))) {
            return new Response
                    .builder()
                    .code(Rcode.FAIL)
                    .msg("密码检测不通过，请修改密码")
                    .data("")
                    .build();
        }
        if (BooleanUtils.isTrue(authServiceImp.isUsernameUsed(user))) {
            return new Response
                    .builder()
                    .code(Rcode.FAIL)
                    .msg("用户名已被使用，请更换用户名")
                    .data("")
                    .build();
        }
        User SecuritiedUser = authServiceImp.newUserConstrator(user);
        if (authServiceImp.insertIntoUserDb(SecuritiedUser)) {
            System.out.println(new LoginData(user, JwtUtil.releaseToken(user)));
            return new Response
                    .builder()
                    .msg("注册成功")
                    .data(new LoginData(SecuritiedUser, JwtUtil.releaseToken(user)))
                    .build();
        }
        return new Response
                .builder()
                .code(Rcode.ERROR)
                .msg("系统错误，请联系管理员")
                .data("")
                .build();
    }

    @PostMapping("/login")
    public Response login(@RequestBody User userSended) {
        boolean matchUserResult = authServiceImp.matchUser(userSended);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", userSended.getUsername());
        User user = userMapper.selectOne(userQueryWrapper);
        if (BooleanUtils.isTrue(matchUserResult)) {
            System.out.println(new LoginData(user, JwtUtil.releaseToken(user)));
            return Response
                    .builder()
                    .msg("登陆成功")
                    .data(new LoginData(user, JwtUtil.releaseToken(user)))
                    .build();
        } else if (BooleanUtils.isFalse(matchUserResult)) {
            return Response
                    .builder()
                    .code(Rcode.FAIL)
                    .msg("密码错误，登陆失败")
                    .data("")
                    .build();
        }
        return new Response
                .builder()
                .code(Rcode.ERROR)
                .msg("系统错误，请联系管理员")
                .data("")
                .build();
    }
}
