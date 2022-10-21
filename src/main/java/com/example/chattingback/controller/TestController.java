package com.example.chattingback.controller;

import com.example.chattingback.Resource.mapper.MybatisPlus;
import com.example.chattingback.eneity.dbEntities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;

@RestController
public class TestController {

    private static JedisPool jedisPool;

    public static void setJedisPool(JedisPool jedisPool) {
        TestController.jedisPool = jedisPool;
    }

    public class a{
        public class b{

        }
    }
    private User[][] users;
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
    public static void main(String[] args) throws IOException {
        Jedis resource = jedisPool.getResource();
        String s = resource.get("cache:group:messages:Echim9的大家庭");
        System.out.println(s);
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
