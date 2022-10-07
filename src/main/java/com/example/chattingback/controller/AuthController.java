package com.example.chattingback.controller;

import com.example.chattingback.eneity.LoginData;
import com.example.chattingback.eneity.Response;
import com.example.chattingback.eneity.dbEntities.User;
import com.example.chattingback.enums.Rcode;
import com.example.chattingback.service.imp.AuthServiceImp;
import com.example.chattingback.service.imp.GroupServiceImp;
import com.example.chattingback.utils.JwtUtil;
import com.example.chattingback.utils.VerifyUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthServiceImp authServiceImp;

    @Autowired
    private GroupServiceImp groupServiceImp;

    @PostMapping("/register")
    public Response register(@RequestBody User user, HttpServletRequest request) {
        Object user1 = request.getAttribute("user");
        Object user2 = request.getSession().getAttribute("user");
        System.out.println("user1" + user1);
        System.out.println("user2" + user2);
        System.out.println("user" + user);
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
            return new Response
                    .builder()
                    .msg("注册成功")
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

    @PostMapping("/login")
    public Response login(@RequestBody User user) {
        if (1 == 1) {

        }
        boolean matchUserResult = authServiceImp.matchUser(user);
        if (BooleanUtils.isTrue(matchUserResult)) {
            String token = jwtUtil.releaseToken(user);
            LoginData loginData = new LoginData();
            loginData.setToken(token);
            loginData.setUser(user);
            return Response
                    .builder()
                    .msg("登陆成功")
                    .data(loginData)
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
