package com.example.chattingback.Resource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.chattingback.eneity.dbEntities.FriendMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Mapper
@Repository
public interface FriendMessageMapper extends BaseMapper<FriendMessage> {

    List<FriendMessage> selectFriendMessagesBySql(@Param("userId") String userId, @Param("friendId") String friendId);

    ArrayList<FriendMessage> selectFriendMessagesBySqlPage(@Param("userId") String userId, @Param("friendId") String friendId, @Param("current") int current, @Param("pagesize") int pagesize);
}
