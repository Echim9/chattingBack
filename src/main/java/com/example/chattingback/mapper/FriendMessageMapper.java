package com.example.chattingback.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.chattingback.eneity.dbEntities.FriendMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FriendMessageMapper extends BaseMapper<FriendMessage> {

    List<FriendMessage> selectFriendMessagesBySql(@Param("userId") String userId, @Param("friendId") String friendId);

    IPage<FriendMessage> selectFriendMessagesBySqlPage(Page page, @Param("userId") String userId, @Param("friendId") String friendId);
}
