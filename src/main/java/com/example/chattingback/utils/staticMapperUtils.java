package com.example.chattingback.utils;

import com.example.chattingback.Resource.mapper.FriendMessageMapper;
import com.example.chattingback.Resource.mapper.GroupMessage;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author Echim9
 * @date 2022/10/21 14:03
 */
@Component
public class staticMapperUtils {

    private static staticMapperUtils staticMapperUtils;

    @Resource
    private FriendMessageMapper friendMessageMapper;

    @Resource
    private GroupMessage groupMessageMapper;

    @PostConstruct
    private void init(){
        staticMapperUtils = this;
        staticMapperUtils.friendMessageMapper = this.friendMessageMapper;
        staticMapperUtils.groupMessageMapper = this.groupMessageMapper;
    }
}
