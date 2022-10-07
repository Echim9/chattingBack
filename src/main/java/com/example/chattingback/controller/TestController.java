package com.example.chattingback.controller;

import com.example.chattingback.mapper.MybatisPlus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    public static void main(String[] args) {

    }
    @Autowired
    private MybatisPlus mybatisPlus;
//    @PostMapping("/auth/login")
//    public Response doit(){
//        Response response = new Response();
//        LoginData loginData = new LoginData();
//        loginData.setUsername("sadfdasdasg");
//        loginData.setPassword("usersadasd");
//        User user = new User();
//        user.setUsername(loginData.getUsername());
//        user.setPassword(loginData.getPassword());
//        int insert = mybatisPlus.insert(user);
//        System.out.println(insert);
//        response.setData(loginData);
//        response.setMsg("登陆成功");
//
//        System.out.println(response);
//        return  response;
//    }
}
