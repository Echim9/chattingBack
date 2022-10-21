package com.example.chattingback.Resource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.chattingback.eneity.dbEntities.UserFriend;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserFriendMapper extends BaseMapper<UserFriend> {
}
