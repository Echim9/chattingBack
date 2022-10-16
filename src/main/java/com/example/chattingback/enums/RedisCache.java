package com.example.chattingback.enums;

import lombok.Data;
import lombok.Getter;

/**
 * @author Echim9
 * @date 2022/10/14 13:35
 */

@Getter
@Data
public class RedisCache {

    /**
    * 此处进行关于redis存储key的规定
     * 存储user：CACHE_USER + userId  主要在user存储之前统一要转为friendDto方便里面存储信息数据
     * 存储user的groups：CACHE_USER_GROUPS + userId
     * 存储user的friends：CACHE_USER_FRIENDS + userId
     * 存储user和friends的聊天信息 CACHE_USER_FRIENDS_MESSAGES + roomID
     * 存储group的聊天信息 CACHE_GROUPS_MESSAGES + groupId
     * 储存群组的基本信息 CACHE_GROUPS + groupID
    * */

    public static final long EXPIRE_TIME = 60 * 60;

    public static final String CACHE_USER = "cache:user:";

    public static final String CACHE_USER_FRIENDS = "cache:user:friends:";


    public static final String CACHE_USER_FRIENDS_MESSAGES = "cache:user:friends:messages:";

    public static final String CACHE_USER_GROUPS= "cache:user:groups:";

    public static final String CACHE_GROUP = "cache:group:";

    public static final String CACHE_GROUPS_MESSAGES = "cache:group:messages:";

}
