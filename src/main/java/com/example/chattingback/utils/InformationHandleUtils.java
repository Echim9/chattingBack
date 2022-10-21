package com.example.chattingback.utils;

import com.example.chattingback.Resource.mapper.FriendMessageMapper;
import com.example.chattingback.enums.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author Echim9
 * @date 2022/10/20 14:31
 */
public class InformationHandleUtils {

    public static void main(String[] args) {
        Set s = RedisUtil.getkeys("*" + RedisCache.CACHE_USER + "*");
        if (!s.isEmpty()){
            s.forEach(s1 -> {
                System.out.println(s1);
            });
        }else {
            System.out.println("1");
        }
    }
    private static Logger logger = LoggerFactory.getLogger(InformationHandleUtils.class);

    @Resource
    private static FriendMessageMapper friendMessageMapper;

    @Resource
    private static com.example.chattingback.Resource.mapper.GroupMessage groupMessageMapper;
    /*
     *传入的类型是应该为message的arraylist，需要注意
     * */

}
