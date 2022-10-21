package com.example.chattingback.Resource;

import com.alibaba.fastjson2.JSONArray;
import com.example.chattingback.Resource.mapper.GroupMessage;
import com.example.chattingback.eneity.dbEntities.FriendMessage;
import com.example.chattingback.enums.RedisCache;
import com.example.chattingback.utils.RedisUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Echim9
 * @date 2022/10/20 15:15
 */

public class RedisToMysqlTask extends QuartzJobBean {

    private JedisPool jedisPool;


    private static Logger logger = LoggerFactory.getLogger(RedisToMysqlTask.class);


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            Jedis resource = jedisPool.getResource();
            //获取群组聊天信息
            String groupMessagesJson = resource.get("*" + RedisCache.CACHE_GROUPS_MESSAGES + "*");
            JSONArray Gjson = JSONArray.parseArray(groupMessagesJson);
            List<GroupMessage> groupMessages = Gjson.toJavaList(GroupMessage.class);
            RedisUtil.redisCacheTOMysql((ArrayList) groupMessages);
            //获取单聊信息
            String friendMessageJson = resource.get("*" + RedisCache.CACHE_USER_FRIENDS_MESSAGES + "*");
            JSONArray Fjson = JSONArray.parseArray(friendMessageJson);
            List<FriendMessage> friendMessages = Fjson.toJavaList(FriendMessage.class);
            RedisUtil.redisCacheTOMysql((ArrayList) friendMessages);
        }catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
