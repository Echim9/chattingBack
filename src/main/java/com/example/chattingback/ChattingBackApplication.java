package com.example.chattingback;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.chattingback.Resource.mapper")
public class ChattingBackApplication {


    public static void main(String[] args) {
        SpringApplication.run(ChattingBackApplication.class, args);

    }

}
