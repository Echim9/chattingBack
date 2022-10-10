package com.example.chattingback.controller.socketIO;

import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class ClientCache {
    /**
     * @Description //TODO 用户信息缓存
     **/

    private static Map<String, HashMap<UUID, SocketIOClient>> concurrentHashMap = new ConcurrentHashMap<>();

    /**
     * @Description //TODO userId-用户ID | sessionId-页面sessionId | socketIOClient-页面对应的通道连接
     **/
    public void saveClient(String userId,UUID sessionId,SocketIOClient socketIOClient){
        HashMap<UUID, SocketIOClient> sessionIdClientCache = concurrentHashMap.get(userId);
        if(sessionIdClientCache == null){
            sessionIdClientCache = new HashMap<>();
        }
        sessionIdClientCache.put(sessionId,socketIOClient);
        concurrentHashMap.put(userId,sessionIdClientCache);
    }

    /**
     * @Description //TODO 获取用户的页面通道信息
     * @Param [userId]
     * @return java.util.HashMap<java.util.UUID,com.corundumstudio.socketio.SocketIOClient>
     **/
    public HashMap<UUID,SocketIOClient> getUserClient(String userId){
        return concurrentHashMap.get(userId);
    }

    /**
     * @Description //TODO 根据用户Id及页面sessionID删除页面通道连接
     * @Param [userId, sessionId]
     * @return void
     **/
    public void deleteSessionClientByUserId(String userId,UUID sessionId){
        concurrentHashMap.get(userId).remove(sessionId);
    }

    /**
     * @Description //TODO 根据用户Id删除用户通道连接缓存 暂无使用
     * @Param [userId]
     * @return void
     **/
    public void deleteUserCacheByUserId(String userId){
        concurrentHashMap.remove(userId);
    }
}
