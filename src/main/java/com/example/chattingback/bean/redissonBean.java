package com.example.chattingback.bean;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Echim9
 * @date 2022/10/16 13:38
 */
@Component
public class redissonBean {
    //布隆过滤器装配初始化

    public RBloomFilter userBloomFilter;

    public RBloomFilter groupBloomFilter;

    @Resource
    private RedissonClient redissonClient;

    @Autowired
    public void userBloomFilterInit(){
        RBloomFilter<Object> userBloomFilter = redissonClient.getBloomFilter("User");
        userBloomFilter.tryInit(100000,0.03);
        this.userBloomFilter = userBloomFilter;
    }
    @Autowired
    public void groupBloomFilterInit(){
        RBloomFilter<Object> groupBloomFilter = redissonClient.getBloomFilter("Group");
        groupBloomFilter.tryInit(100000,0.03);
        this.groupBloomFilter = groupBloomFilter;
    }
}
