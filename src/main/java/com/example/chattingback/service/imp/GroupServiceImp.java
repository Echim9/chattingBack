package com.example.chattingback.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.chattingback.eneity.dbEntities.Group;
import com.example.chattingback.eneity.response.Response;
import com.example.chattingback.eneity.dbEntities.User;
import com.example.chattingback.eneity.dbEntities.UserGroup;
import com.example.chattingback.enums.Rcode;
import com.example.chattingback.mapper.GroupMapper;
import com.example.chattingback.mapper.UserGroupMapper;
import com.example.chattingback.mapper.UserMapper;
import com.example.chattingback.service.GroupService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImp implements GroupService {

    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private UserGroupMapper userGroupMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean AddToGroup(User user, Group group) {
        UserGroup userGroup = new UserGroup();
        userGroup.setGroupId(group.getGroupId());
        userGroup.setUserId(user.getUserId());
        int insertResult = userGroupMapper.insert(userGroup);
        if (insertResult != 0) {
            return true;
        }
        return false;
    }


    @Override
    public Response InitGroup(Group group) {
        System.out.println("group" + group);
        int insertResult = groupMapper.insert(group);
        System.out.println(insertResult);
        if (insertResult == 1) {
            return Response
                    .builder()
                    .msg("群组创建成功")
                    .data("")
                    .build();
        } else if (insertResult != 1) {
            return Response
                    .builder()
                    .code(Rcode.FAIL)
                    .msg("群组创建失败")
                    .data("")
                    .build();
        }
        return Response
                .builder()
                .code(Rcode.ERROR)
                .msg("群组创建错误，请联系管理员")
                .data("")
                .build();
    }

    @Override
    public boolean isGroupAppeared(Group group) {
        QueryWrapper<Group> groupQueryWrapper = new QueryWrapper<>();
        groupQueryWrapper.setEntity(group);
        Object selectResult = groupMapper.selectOne(groupQueryWrapper);
        if (ObjectUtils.isEmpty(selectResult)) {
            return false;
        }
        return true;
    }

    @Override
    public Response findGroupByName(Group group) {
        QueryWrapper<Group> groupQueryWrapper = new QueryWrapper<>();
        groupQueryWrapper
                .isNotNull("groupName")
                .like("groupName", group.getGroupName());
        List<Group> groupList = groupMapper.selectList(groupQueryWrapper);
        if (groupList.size() > 0) {
            return Response
                    .builder()
                    .data(groupList)
                    .build();
        } else if (groupList.size() == 0) {
            return Response
                    .builder()
                    .code(Rcode.FAIL)
                    .msg("查无此群")
                    .data(null)
                    .build();
        }
        return Response
                .builder()
                .code(Rcode.ERROR)
                .msg("查找群错误")
                .data(null)
                .build();
    }

    @Override
    public Response getUserGroups(User user) {
        QueryWrapper<UserGroup> userGroupQueryWrapper = new QueryWrapper<>();
        userGroupQueryWrapper
                .isNotNull("userId")
                .eq("userId", user.getUserId());
        List<UserGroup> userGroupsIds = userGroupMapper.selectList(userGroupQueryWrapper);
        ArrayList<Group> groups = new ArrayList<>();
        QueryWrapper<Group> groupQueryWrapper = new QueryWrapper<>();
        userGroupsIds.forEach(userGroupId -> groups.add(groupMapper.selectOne(groupQueryWrapper.eq("groupId", userGroupId))));
        if (ObjectUtils.isEmpty(groups)) {
            return Response
                    .builder()
                    .code(Rcode.FAIL)
                    .msg("未查找到所加入群组")
                    .data(null)
                    .build();
        } else if (!ObjectUtils.isEmpty(groups)) {
            return Response
                    .builder()
                    .msg("查找成功")
                    .data(groups)
                    .build();
        }
        return Response
                .builder()
                .code(Rcode.ERROR)
                .msg("群组查找失败")
                .data(null)
                .build();
    }

    @Override
    public Response getGroupMessages(Group group) {
        return null;
    }

    @Override
    public Response getGroupUsers(Group group) {
        QueryWrapper<UserGroup> userGroupQueryWrapper = new QueryWrapper<>();
        userGroupQueryWrapper
                .isNotNull("userId")
                .isNotNull("groupId")
                .eq("groupId", group.getGroupId());
        List<UserGroup> usersInGroup = userGroupMapper.selectList(userGroupQueryWrapper);
        ArrayList<User> users = new ArrayList<>();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        usersInGroup.forEach(user -> users.add(userMapper.selectOne(userQueryWrapper.eq("userId", user.getUserId()))));
        if (ObjectUtils.isEmpty(users)) {
            return Response
                    .builder()
                    .code(Rcode.FAIL)
                    .msg("未查找到群组用户")
                    .data(null)
                    .build();
        } else if (!ObjectUtils.isEmpty(users)) {
            return Response
                    .builder()
                    .msg("群组用户查找成功")
                    .data(users)
                    .build();
        }
        return Response
                .builder()
                .code(Rcode.ERROR)
                .msg("群组用户查找失败")
                .data(null)
                .build();
    }
}
