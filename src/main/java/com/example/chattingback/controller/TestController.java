package com.example.chattingback.controller;

import com.example.chattingback.mapper.MybatisPlus;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    public static String strToASCII(String str) {
        StringBuilder sb = new StringBuilder();
        char[] ch = str.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            int i1 = Integer.valueOf(ch[i]).intValue();
            String s = Integer.toHexString(i1);
            sb.append(s);
        }
        return sb.toString();

    }
    public static void main(String[] args) {
        String random = RandomStringUtils.random(5,"qwertyuiopasdfghjklzxcvbnm1234567890");
        System.out.println(random);

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
