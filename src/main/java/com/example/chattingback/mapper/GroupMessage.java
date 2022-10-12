package com.example.chattingback.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GroupMessage extends BaseMapper<com.example.chattingback.eneity.dbEntities.GroupMessage> {

}
