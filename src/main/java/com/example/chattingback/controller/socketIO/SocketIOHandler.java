package com.example.chattingback.controller.socketIO;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.example.chattingback.Resource.mapper.*;
import com.example.chattingback.bean.redissonBean;
import com.example.chattingback.eneity.chat.FriendDto;
import com.example.chattingback.eneity.chat.FriendMessageDto;
import com.example.chattingback.eneity.chat.GroupDto;
import com.example.chattingback.eneity.chat.GroupMessageDto;
import com.example.chattingback.eneity.dbEntities.GroupMessage;
import com.example.chattingback.eneity.dbEntities.*;
import com.example.chattingback.eneity.payload;
import com.example.chattingback.eneity.response.JoinRes;
import com.example.chattingback.eneity.response.Response;
import com.example.chattingback.enums.Rcode;
import com.example.chattingback.utils.MyBeanUtils;
import com.example.chattingback.utils.RedisUtil;
import com.example.chattingback.utils.imageUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.chattingback.enums.RedisCache.*;

@Component
public class SocketIOHandler {

    private static Logger logger = LoggerFactory.getLogger(SocketIOHandler.class);


    @Resource
    private SocketIOServer socketIOServer;

    private static final String DEFAULT_ROOM = "Echim9????????????";

    @Resource
    private  com.example.chattingback.Resource.mapper.GroupMessage groupMessageMapper;

    @Resource
    private RedissonClient redissonClient;


    private imageUtils imageutils;

    @Resource
    private  FriendMessageMapper friendMessageMapper;


    @Resource
    private UserFriendMapper userFriendMapper;

    @Resource
    private UserGroupMapper userGroupMapper;

    @Resource
    private GroupMapper groupMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ClientCache clientCache;

    @Resource
    private redissonBean redissonBean;

    /**
     * @Description //TODO ???????????????????????????????????????js?????????socket = io.connect("http://localhost:3000");
     **/
    @OnConnect
    public void onConnect(SocketIOClient client) {
        String userId = client.getHandshakeData().getSingleUrlParam("userId");
        getActiveGroupUser();
        //?????????????????????????????????
        client.joinRoom(userId);
        //??????????????????????????????
        client.joinRoom(DEFAULT_ROOM);
        UUID sessionId = client.getSessionId();
        clientCache.saveClient(userId, sessionId, client);
        logger.info("userId:{}????????????,sessionID{}", userId, sessionId);
    }

    /**
     * @Description //TODO ???????????????????????????????????????js?????????socket.disconnect();
     **/
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String userId = client.getHandshakeData().getSingleUrlParam("userId");
        UUID sessionId = client.getSessionId();
        clientCache.deleteSessionClientByUserId(userId, sessionId);
        getActiveGroupUser();
        logger.info("userId:{}??????????????????,sessionId{}", userId, sessionId);

    }

    /**
     * @Description //TODO ?????????????????????????????????js??????:socket.emit('messageevent', {msgContent: msg});??????????????????
     * //TODO ??????js??? socket.emit("?????????","????????????")???????????????????????????????????????????????????????????????
     * //TODO ??????js??? socket.on("?????????",????????????(????????????????????????????????????))??????????????????????????????
     **/

    //2022.10.10????????????
    @OnEvent("chatData")
    public void chatEvent(SocketIOClient client, AckRequest ackRequest, User user) {
        /**
         * ????????????????????????????????????redis????????????????????????????????????????????????
         *
         * */
        //???????????????
        RBloomFilter userBloomFilter = redissonBean.userBloomFilter;
//        RBloomFilter groupBloomFilter = redissonBean.groupBloomFilter;
        getActiveGroupUser();
        //???????????????
        ArrayList<FriendDto> userArr = new ArrayList<>();
        ArrayList<GroupDto> groupArrayList = new ArrayList<>();
        ArrayList<FriendDto> friendArrayList = new ArrayList<>();
        HashMap<String, User> userHashMap = new HashMap<>();
        HashMap<String, ArrayList<GroupMessageDto>> groupMessagesArray = new HashMap<>();
        HashMap<FriendDto, ArrayList<FriendMessageDto>> friendMessageArray = new HashMap<>();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userId", user.getUserId());
        User isUser = userMapper.selectOne(userQueryWrapper);
        //????????????????????????????????????????????????
//        String userString = RedisUtil.get("*" + CACHE_USER + "*");
//        if (StringUtils.isNotBlank(userString)) {
//            JSONArray userJsonArr = JSONArray.parseArray(userString);
//            List<User> users = userJsonArr.toJavaList(User.class);
//            users.forEach(theUser -> {
//                if (!userBloomFilter.contains(theUser.getUserId())){
//                    userBloomFilter.add(theUser.getUserId());
//                }
//            });
//        }else {
//            ArrayList<User> userArrayList = userMapper.selectAll();
//            userArrayList.forEach(theUser -> {
//                if (!userBloomFilter.contains(theUser.getUserId())){
//                    userBloomFilter.add(theUser.getUserId());
//                }
//            });
//        }
//        String groupString = RedisUtil.get("*" + CACHE_GROUP + "*");
//        if (StringUtils.isNotBlank(groupString)) {
//            JSONArray groupJSONArray = JSONArray.parseArray(userString);
//            List<Group> groups = groupJSONArray.toJavaList(Group.class);
//            groups.forEach(theGroup -> {
//                if (!userBloomFilter.contains(theGroup.getGroupId())){
//                    userBloomFilter.add(theGroup.getGroupId());
//                }
//            });
//        }else {
//            ArrayList<Group> groups = groupMapper.selectAll();
//            groups.forEach(theGroup -> {
//                if (!userBloomFilter.contains(theGroup.getGroupId())){
//                    userBloomFilter.add(theGroup.getGroupId());
//                }
//            });
//        }
        if (ObjectUtils.isNotEmpty(isUser)) {
            ArrayList<UserGroup> userGroups = new ArrayList<>();
            //?????????????????????????????????
//            String userGroupsResult = RedisUtil.get(CACHE_USER_GROUPS + isUser.getUserId());
//            if (StringUtils.isNotEmpty(userGroupsResult)) {
//                //System.out.println(userGroupsResult);
//                JSONArray jsonArray = JSONArray.parse(userGroupsResult);
//                List<UserGroup> userGroupsResults = jsonArray.toJavaList(UserGroup.class);
//                userGroups.addAll(userGroupsResults);
//            } else {
            QueryWrapper<UserGroup> userGroupQueryWrapper = new QueryWrapper<>();
            userGroupQueryWrapper.eq("userId", user.getUserId());
            List<UserGroup> DBuserGroups = userGroupMapper.selectList(userGroupQueryWrapper);
            userGroups.addAll(DBuserGroups);
            RedisUtil.set(CACHE_USER_GROUPS + isUser.getUserId(), JSONObject.toJSONString(userGroups), EXPIRE_TIME);
//            }
            ArrayList<UserFriend> userFriends = new ArrayList<>();
//            String userFriendsResult = RedisUtil.get(CACHE_USER_FRIENDS + isUser.getUserId());
//            if (StringUtils.isNotEmpty(userFriendsResult)) {
//                JSONArray jsonArray = JSONArray.parse(userFriendsResult);
//                List<UserFriend> userFriendsResultRedis = jsonArray.toJavaList(UserFriend.class);
//                userFriends.addAll(userFriendsResultRedis);
//            } else {
            //??????????????????
            QueryWrapper<UserFriend> userFriendQueryWrapper = new QueryWrapper<>();
            userFriendQueryWrapper.eq("userId", user.getUserId());
            List<UserFriend> DBUserFriends = userFriendMapper.selectList(userFriendQueryWrapper);
            userFriends.addAll(DBUserFriends);
            RedisUtil.set(CACHE_USER_FRIENDS + isUser.getUserId(), JSONObject.toJSONString(userFriends), EXPIRE_TIME);
//            }
            //??????????????????????????????????????????
            userGroups.forEach(group -> {
                if (1 == 0) {
                    System.out.println("=====================group===============================================");
                    System.out.println(group);
                    System.out.println("========================================================================");
                }
//                String groupResult = RedisUtil.get(CACHE_GROUP + group.getGroupId());
//                if (StringUtils.isNotEmpty(groupResult)) {
//                    JSONObject jsonObject = JSONObject.parseObject(groupResult);
//                    groupArrayList.add(jsonObject.toJavaObject(GroupDto.class));
//                } else {
                GroupDto groupDto = MyBeanUtils.copyProperties(groupMapper.selectOne(new QueryWrapper<Group>().eq("groupId", group.getGroupId())), GroupDto.class);
                groupArrayList.add(groupDto);
                RedisUtil.set(CACHE_GROUP + group.getGroupId(), JSONObject.toJSONString(groupDto), EXPIRE_TIME);
//                }

            });
            //??????????????????????????????????????????
            int size2 = groupArrayList.size();
            for (int i = 0; i < size2; i++) {
                GroupDto groupDto = groupArrayList.get(i);
                if (1 == 0) {
                    System.out.println("===============================================================================");
                    System.out.println("size = " + size2);
                    System.out.println(groupArrayList);
                    System.out.println(groupDto);
                    System.out.println("===============================================================================");
                }
//                String groupMessageResult = RedisUtil.get(CACHE_GROUPS_MESSAGES + groupDto.getGroupId());
                ArrayList<GroupMessageDto> groupMessageDtos = new ArrayList<>();
//                if (StringUtils.isNotEmpty(groupMessageResult)) {
//                    JSONObject jsonObject = JSONObject.parseObject(groupMessageResult);
//                    groupMessageDtos.add(jsonObject.toJavaObject(GroupMessageDto.class));
//                } else {
                ArrayList<GroupMessage> messages = new ArrayList<>();
                QueryWrapper<GroupMessage> groupMessageDtoQueryWrapper = new QueryWrapper<>();
                groupMessageDtoQueryWrapper.eq("groupId", groupDto.getGroupId());
                List<GroupMessage> groupMessagesFromMysql = groupMessageMapper.selectList(groupMessageDtoQueryWrapper);
                if (ObjectUtils.isNotEmpty(groupMessagesFromMysql)) {
                    messages.addAll(groupMessagesFromMysql);
                }
                ArrayList<GroupMessage> groupMessagesFromRedis = RedisUtil.getGroupMessagesFromRedis(groupDto.getGroupId());
                if (ObjectUtils.isNotEmpty(groupMessagesFromRedis)){
                    messages.addAll(groupMessagesFromRedis);
                }
                messages.forEach(groupMessage -> groupMessageDtos.add(MyBeanUtils.copyProperties(groupMessage, GroupMessageDto.class)));
                groupMessagesArray.put(groupDto.getGroupId(), groupMessageDtos);
                //?????????????????????????????????????????????
                int size = groupMessageDtos.size();
                for (int j = 0; j < size; j++) {
//                    String userOfMessages = RedisUtil.get(CACHE_USER + groupMessageDtos.get(j).getUserId());
//                    if (StringUtils.isNotEmpty(userOfMessages)) {
//                        JSONObject jsonObject = JSONObject.parseObject(userOfMessages);
//                        userHashMap.put(groupMessageDtos.get(j).getUserId(), jsonObject.toJavaObject(User.class));
//                    } else {
                    QueryWrapper<User> userQueryWrapper1 = new QueryWrapper<>();
                    userQueryWrapper1.eq("userId", groupMessageDtos.get(j).getUserId());
                    User thisUser = userMapper.selectOne(userQueryWrapper1);
                    userHashMap.put(groupMessageDtos.get(j).getUserId(), thisUser);
                    RedisUtil.set(CACHE_USER + groupMessageDtos.get(j).getUserId(), JSONObject.toJSONString(MyBeanUtils.copyProperties(thisUser, FriendDto.class)), EXPIRE_TIME);
//                    }
                }
            }
            //???????????????????????????????????? ???????????????groupArrayList ?????????????????????groupMessagesArray
            for (int i = 0; i < groupArrayList.size(); i++) {
                String groupId = groupArrayList.get(i).getGroupId();
                ArrayList<GroupMessageDto> groupMessageDtos = groupMessagesArray.get(groupId);
                GroupMessageDto[] messageDtos = groupMessageDtos.toArray(new GroupMessageDto[groupMessageDtos.size()]);
                groupArrayList.get(i).setMessages(messageDtos);
            }
            //?????????????????????????????????
            userFriends.forEach(userFriend -> {
//                String userFriendResult = RedisUtil.get(CACHE_USER + userFriend.getFriendId());
//                if (StringUtils.isNotBlank(userFriendResult)) {
//                    JSONObject jsonObject = JSONObject.parseObject(userFriendResult);
//                    friendArrayList.add(jsonObject.toJavaObject(FriendDto.class));
//                } else {
                FriendDto friendDto = MyBeanUtils.copyProperties(userMapper.selectOne(new QueryWrapper<User>().eq("userId", userFriend.getFriendId())), FriendDto.class);
                friendArrayList.add(friendDto);
                RedisUtil.set(CACHE_USER + friendDto.getUserId(), JSONObject.toJSONString(friendDto), EXPIRE_TIME);
//                }
            });
            //????????????????????????????????????
            int size1 = userFriends.size();
            for (int i = 0; i < size1; i++) {
                if (1 == 0) {
                    System.out.println("=======================================userFriends==============================");
                    System.out.println("size = " + size1);
                    System.out.println("i = " + i);
                    System.out.println(userFriends);
                    System.out.println("??????userFriend = " + userFriends.get(i));
                    System.out.println("===============================================================================");
                }
                String roomId;
                ArrayList<FriendMessageDto> friendMessageDtosTmp = new ArrayList<>();
                if (userFriends.get(i).getUserId().compareTo(userFriends.get(i).getFriendId()) > 0) {
                    roomId = userFriends.get(i).getUserId() + userFriends.get(i).getFriendId();
                } else {
                    roomId = userFriends.get(i).getFriendId() + userFriends.get(i).getUserId();
                }
//                String result = RedisUtil.get(CACHE_USER_FRIENDS_MESSAGES + roomId);
//                if (StringUtils.isNotBlank(result)) {
//                    JSONArray jsonArray = JSONArray.parseArray(result);
//                    friendMessageDtosTmp.addAll(jsonArray.toJavaList(FriendMessageDto.class));
//                } else {
                ArrayList<FriendMessage> messages = new ArrayList<>();
                List<FriendMessage> friendMessagesFromMysql = friendMessageMapper.selectFriendMessagesBySql(userFriends.get(i).getUserId(), userFriends.get(i).getFriendId());
                if (ObjectUtils.isNotEmpty(friendMessagesFromMysql)) {
                    messages.addAll(friendMessagesFromMysql);
                }
                ArrayList<FriendMessage> friendMessagesFromRedis = RedisUtil.getFriendMessagesFromRedis(roomId);
                if (ObjectUtils.isNotEmpty(friendMessagesFromRedis)){
                    messages.addAll(friendMessagesFromRedis);
                }
                messages.forEach(friendMessage -> {
                    FriendMessageDto messageDto = MyBeanUtils.copyProperties(friendMessage, FriendMessageDto.class);
                    friendMessageDtosTmp.add(messageDto);
                });
//                RedisUtil.set(CACHE_USER_FRIENDS_MESSAGES + roomId, JSONObject.toJSONString(friendMessageDtosTmp), EXPIRE_TIME);

//                QueryWrapper<FriendMessage> friendMessageQueryWrapper1 = new QueryWrapper<>();
//                friendMessageQueryWrapper1.eq("userId", userFriends.get(i).getUserId()).eq("friendId", userFriends.get(i).getFriendId()).orderByDesc("time");
//                QueryWrapper<FriendMessage> friendMessageQueryWrapper2 = new QueryWrapper<>();
//                friendMessageQueryWrapper2.eq("userId", userFriends.get(i).getFriendId()).eq("friendId", userFriends.get(i).getUserId()).orderByDesc("time");
//                List<FriendMessage> friendMessages1 = friendMessageMapper.selectList(friendMessageQueryWrapper1);
//                List<FriendMessage> friendMessages2 = friendMessageMapper.selectList(friendMessageQueryWrapper2);
//                friendMessages2.forEach(friendMessage -> {
//                    friendMessageDtosTmp.add(MyBeanUtils.copyNotNullProperties(friendMessage, FriendMessageDto.class));
//                });
//                friendMessages1.forEach(friendMessage -> {
//                    friendMessageDtosTmp.add(MyBeanUtils.copyNotNullProperties(friendMessage, FriendMessageDto.class));
//                });
                friendMessageArray.put(friendArrayList.get(i), friendMessageDtosTmp);
            }
            //?????????????????????????????????????????? ???????????????friendArrayList ?????????????????????friendMessageArray
            for (int i = 0; i < friendArrayList.size(); i++) {
                FriendDto friendDto = friendArrayList.get(i);
                ArrayList<FriendMessageDto> messageDtos = friendMessageArray.get(friendDto);
                FriendMessageDto[] dtos = messageDtos.toArray(new FriendMessageDto[messageDtos.size()]);
                friendDto.setMessages(dtos);
            }
            userHashMap.forEach((id, inUser) -> {
                userArr.add(MyBeanUtils.copyNotNullProperties(inUser, FriendDto.class));
            });
            friendArrayList.forEach((friendDto -> {
                userArr.add(friendDto);
            }));

            payload payload = new payload();
            payload.setFriendData(friendArrayList);
            payload.setUserData(userArr);
            payload.setGroupData(groupArrayList);
            if (1 == 0) {
                System.out.println("==============================allDataRes============================");
                System.out.println(payload);
                System.out.println("===============================================================");
            }
            client.sendEvent("chatData",
                    new Response()
                            .builder()
                            .msg("????????????????????????")
                            .data(payload)
                            .build());
        }
    }

    //2022.10.9????????????
    @OnEvent("addGroup")
    public void addGroup(SocketIOClient client, AckRequest ackRequest, Group group) {
        UserGroup userGroup = new UserGroup();
        userGroup.setUserId(group.getUserId());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper
                .isNotNull("userId")
                .eq("userId", group.getUserId());
        if (1 == 0) {
            System.out.println("group:" + group);
            System.out.println("user:" + group.getUserId());
        }
        User isUser = userMapper.selectOne(userQueryWrapper);
        if (!ObjectUtils.isEmpty(isUser)) {
            QueryWrapper<Group> groupQueryWrapper = new QueryWrapper<>();
            groupQueryWrapper.eq("groupName", group.getGroupName());
            Group isGroup = groupMapper.selectOne(groupQueryWrapper);
            if (ObjectUtils.isEmpty(isGroup)) {
                group.setGroupId(UUID.randomUUID().toString());
                int result = groupMapper.insert(group);
                if (result > 0) {
                    client.joinRoom(group.getGroupId());
                    //System.out.println(userGroup);
                    userGroup.setGroupId(group.getGroupId());
                    int addRes = userGroupMapper.insert(userGroup);
                    if (addRes != 0) {
                        //???????????????????????????????????????
                        redissonBean.groupBloomFilter.add(group.getGroupId());
                        client.sendEvent("addGroup",
                                new Response()
                                        .builder()
                                        .msg("???????????????" + group.getGroupName())
                                        .data(group)
                                        .build());
                    }
                    getActiveGroupUser();
                    return;
                }
            } else if (!ObjectUtils.isEmpty(isGroup)) {
                client.sendEvent("addGroup",
                        new Response()
                                .builder()
                                .msg("??????????????????")
                                .code(Rcode.FAIL)
                                .data("")
                                .build()
                );
                return;
            }
        } else {
            //System.out.println(isUser);
            client.sendEvent("addGroup",
                    new Response()
                            .builder()
                            .msg("?????????????????????????????????")
                            .code(Rcode.ERROR)
                            .data("")
                            .build()
            );
            return;
        }
    }

    //2022.10.9???????????????????????????????????????
    @OnEvent("joinGroup")
    public void joinGroup(SocketIOClient client, AckRequest ackRequest, UserGroup usergroup) {
        if (1 == 0) {
            System.out.println("usergroup===============================" + usergroup);
            System.out.println("group:" + usergroup.getGroupId());
            System.out.println("user:" + usergroup.getUserId());
        }
//        if (redissonBean.groupBloomFilter.contains(usergroup.getGroupId())) {
        String uID = RedisUtil.get(CACHE_USER + usergroup.getUserId());
        User isUser = new User();
        if (StringUtils.isEmpty(uID)) {
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper
                    .isNotNull("userId")
                    .eq("userId", usergroup.getUserId());
            isUser = userMapper.selectOne(userQueryWrapper);
            RedisUtil.set(CACHE_USER + usergroup.getUserId(), JSONObject.toJSONString(MyBeanUtils.copyProperties(isUser, FriendDto.class)), EXPIRE_TIME);
        } else {
            isUser = JSONObject.parseObject(uID, User.class);
        }
        JoinRes joinRes = new JoinRes();
        if (!ObjectUtils.isEmpty(isUser)) {
            joinRes.setUser(isUser);
            Group isGroup = new Group();
            String groupJson = RedisUtil.get(CACHE_GROUP + usergroup.getGroupId());
            if (StringUtils.isEmpty(groupJson)) {
                QueryWrapper<Group> GroupQueryWrapper = new QueryWrapper<>();
                GroupQueryWrapper.eq("groupId", usergroup.getGroupId());
                isGroup = groupMapper.selectOne(GroupQueryWrapper);
                RedisUtil.set(CACHE_GROUP + usergroup.getGroupId(), JSONObject.toJSONString(isGroup), EXPIRE_TIME);
            } else {
                isGroup = JSONObject.parseObject(groupJson, new Group().getClass());
            }
            if (!ObjectUtils.isEmpty(isGroup)) {
                joinRes.setGroup(isGroup);
                QueryWrapper<UserGroup> userGroupQueryWrapper = new QueryWrapper<>();
                userGroupQueryWrapper.eq("userId", usergroup.getUserId()).eq("groupId", usergroup.getGroupId());
                if (ObjectUtils.isEmpty(userGroupMapper.selectOne(userGroupQueryWrapper))) {
                    int result = userGroupMapper.insert(usergroup);
                    if (result > 0) {
                        client.joinRoom(isGroup.getGroupId());
                        client.sendEvent("joinGroup",
                                new Response()
                                        .builder()
                                        .msg(isUser.getUsername() + "?????????????????????" + isGroup.getGroupName())
                                        .data(joinRes)
                                        .build()
                        );
                        getActiveGroupUser();
                        return;
                    }
                } else {
                    client.sendEvent("joinGroup",
                            new Response()
                                    .builder()
                                    .code(Rcode.FAIL)
                                    .msg(isUser.getUsername() + "???????????????" + isGroup.getGroupName())
                                    .data(usergroup)
                                    .build()
                    );
                    return;
                }
            } else {
                client.sendEvent("joinGroup",
                        new Response()
                                .builder()
                                .code(Rcode.FAIL)
                                .msg("???????????????")
                                .data("")
                                .build()
                );
                return;
            }
        } else {
            client.sendEvent("joinGroup",
                    new Response()
                            .builder()
                            .code(Rcode.ERROR)
                            .msg("???????????????????????????????????????")
                            .data("")
                            .build()
            );
            return;
        }
//        }else {
//            client.sendEvent("joinGroup",
//                    new Response()
//                            .builder()
//                            .code(Rcode.ERROR)
//                            .msg("???????????????")
//                            .data("")
//                            .build()
//            );
//            return;
//        }
    }


    //2022???10.9????????????
    @OnEvent("joinGroupSocket")
    public void joinGroupSocket(SocketIOClient client, AckRequest ackRequest, UserGroup usergroup) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userId", usergroup.getUserId());
        QueryWrapper<Group> groupQueryWrapper = new QueryWrapper<>();
        groupQueryWrapper.eq("groupId", usergroup.getGroupId());
        User user = userMapper.selectOne(userQueryWrapper);
        Group group = groupMapper.selectOne(groupQueryWrapper);
        JoinRes joinRes = new JoinRes(group, user);
        getActiveGroupUser();
        if (!ObjectUtils.isEmpty(group) && !ObjectUtils.isEmpty(user)) {
            client.joinRoom(group.getGroupId());
            client.sendEvent("joinGroupSocket",
                    new Response()
                            .builder()
                            .msg(user.getUsername() + "????????????" + group.getGroupName())
                            .data(joinRes)
                            .build()
            );
        } else {
            if (1 == 0) {
                System.out.println("=======================??????????????????==========================");
                System.out.println(group);
                System.out.println(user);
                System.out.println("===========================================================");
            }

            client.sendEvent("joinGroupSocket",
                    new Response()
                            .builder()
                            .code(Rcode.FAIL)
                            .msg("??????????????????")
                            .data(joinRes)
                            .build()
            );
        }
    }

    //2022.10.9??????????????????????????????????????????
    @OnEvent("addFriend")
    public void addFriend(SocketIOClient client, AckRequest ackRequest, UserFriend userFriend) {
        // System.out.println(userFriend.getFriendId());
//        boolean contains = redissonBean.userBloomFilter.contains(userFriend.getFriendId());
//        if (Boolean.TRUE.equals(contains)) {
        QueryWrapper<User> userQueryWrapper1 = new QueryWrapper<>();
        QueryWrapper<User> userQueryWrapper2 = new QueryWrapper<>();
        userQueryWrapper1.eq("userId", userFriend.getUserId());
        userQueryWrapper2.eq("userId", userFriend.getFriendId());
        User friend = userMapper.selectOne(userQueryWrapper2);
        User user = userMapper.selectOne(userQueryWrapper1);
        if (1 == 0) {
            System.out.println("userFriend:" + userFriend);
            System.out.println("addfriend:" + friend);
            System.out.println("adduser:" + user);
        }
        String roomId = null;
        if (ObjectUtils.isNotEmpty(user)) {
            if (ObjectUtils.isNotEmpty(user) && ObjectUtils.isNotEmpty(friend)) {
                if (user.equals(friend)) {
                    client.sendEvent("addFriend",
                            new Response()
                                    .builder()
                                    .code(Rcode.FAIL)
                                    .msg("???????????????????????????")
                                    .data("")
                                    .build());
                }
                QueryWrapper<UserFriend> userFriendQueryWrapper1 = new QueryWrapper<>();
                QueryWrapper<UserFriend> userFriendQueryWrapper2 = new QueryWrapper<>();
                userFriendQueryWrapper1.eq("userId", userFriend.getUserId()).eq("friendId", userFriend.getFriendId());
                userFriendQueryWrapper2.eq("userId", userFriend.getFriendId()).eq("friendId", userFriend.getUserId());
                UserFriend relation1 = userFriendMapper.selectOne(userFriendQueryWrapper1);
                UserFriend relation2 = userFriendMapper.selectOne(userFriendQueryWrapper2);
                if (1 == 0) {
                    System.out.println("=======================================================================");
                    //System.out.println(MyBeanUtils.strToASCII(userFriend.getUserId()) + ":" + MyBeanUtils.strToASCII(userFriend.getFriendId()));
                    System.out.println("=======================================================================");
                }
                if (userFriend.getFriendId().compareTo(userFriend.getUserId()) > 0) {
                    roomId = userFriend.getFriendId() + userFriend.getUserId();
                } else {
                    roomId = userFriend.getUserId() + userFriend.getFriendId();
                }
                if (ObjectUtils.isNotEmpty(relation1) || ObjectUtils.isNotEmpty(relation2)) {
                    client.sendEvent("addFriend",
                            new Response()
                                    .builder()
                                    .code(Rcode.FAIL)
                                    .msg("???????????????")
                                    .data("")
                                    .build());
                    return;
                }
            } else {
                client.sendEvent("addFriend",
                        new Response()
                                .builder()
                                .code(Rcode.FAIL)
                                .msg("??????????????????")
                                .data("")
                                .build());
                return;
            }
            UserFriend userFriendReverse = new UserFriend();
            userFriendReverse.setFriendId(userFriend.getUserId());
            userFriendReverse.setUserId(userFriend.getFriendId());
            userFriendMapper.insert(userFriendReverse);
            int result = userFriendMapper.insert(userFriend);
            //????????????
            if (result > 0) {
                client.joinRoom(roomId);
                QueryWrapper<FriendMessage> friendMessageQueryWrapper1 = new QueryWrapper<>();
                friendMessageQueryWrapper1.eq("userId", userFriend.getUserId()).eq("friendId", userFriend.getFriendId()).orderByDesc("time");
                QueryWrapper<FriendMessage> friendMessageQueryWrapper2 = new QueryWrapper<>();
                friendMessageQueryWrapper2.eq("userId", userFriend.getFriendId()).eq("friendId", userFriend.getUserId()).orderByDesc("time");
                List<FriendMessage> friendMessages1 = friendMessageMapper.selectList(friendMessageQueryWrapper1);
                List<FriendMessage> friendMessages2 = friendMessageMapper.selectList(friendMessageQueryWrapper2);
                List<FriendMessageDto> friendMessageDtos = new ArrayList<>();
                if (friendMessages1.size() > 0) {
                    BeanUtils.copyProperties(friendMessages1, friendMessageDtos);
                } else {
                    BeanUtils.copyProperties(friendMessages2, friendMessageDtos);
                }
                FriendDto userDto = FriendDto.initializeFriendDto(user);
                FriendDto friendDto = FriendDto.initializeFriendDto(friend);
                FriendMessageDto friendMessageDto = new FriendMessageDto();
                FriendMessage defaultMessage = FriendMessage.defaultMessage(user, friend);
                friendMessageMapper.insert(defaultMessage);
                client.sendEvent("addFriend", new Response().builder().data(friendDto).msg(user.getUsername() + "????????????" + friend.getUsername() + "?????????").build());
                if (ObjectUtils.isNotEmpty(clientCache.getUserClient(friend.getUserId()))) {
                    HashMap<UUID, SocketIOClient> userClient = clientCache.getUserClient(friend.getUserId());
                    userClient.forEach((UUID, SocketIOClient) -> {
                        SocketIOClient.sendEvent("addFriend",
                                new Response()
                                        .builder()
                                        .data(userDto)
                                        .msg(user.getUsername() + "????????????????????????")
                                        .build());
                    });
                }
                return;
            }
        } else {
            client.sendEvent("addFriend",
                    new Response()
                            .builder()
                            .code(Rcode.ERROR)
                            .msg("????????????????????????")
                            .data("")
                            .build());
            return;
        }
//        } else {
//            client.sendEvent("addFriend",
//                    new Response()
//                            .builder()
//                            .code(Rcode.FAIL)
//                            .msg("???????????????")
//                            .data("")
//                            .build());
//            return;
//        }
    }

    //2022.10.9??????????????????????????????????????????
    @OnEvent("joinFriendSocket")
    public void addFriendSocket(SocketIOClient client, AckRequest ackRequest, UserFriend userFriend) {
        logger.info("??????{}????????????{}????????????", userFriend.getUserId(), userFriend.getFriendId());
        String roomId;
        if (StringUtils.isNotEmpty(userFriend.getFriendId()) && StringUtils.isNotEmpty(userFriend.getUserId())) {
            QueryWrapper<UserFriend> userFriendQueryWrapper1 = new QueryWrapper<>();
            QueryWrapper<UserFriend> userFriendQueryWrapper2 = new QueryWrapper<>();
            userFriendQueryWrapper1.eq("userId", userFriend.getUserId()).eq("friendId", userFriend.getFriendId());
            userFriendQueryWrapper2.eq("userId", userFriend.getFriendId()).eq("friendId", userFriend.getUserId());
            UserFriend relation1 = userFriendMapper.selectOne(userFriendQueryWrapper1);
            UserFriend relation2 = userFriendMapper.selectOne(userFriendQueryWrapper2);
            if (ObjectUtils.isNotEmpty(relation1) || ObjectUtils.isNotEmpty(relation2)) {
                //??????friend??????user???friend+user????????????
                if (userFriend.getFriendId().compareTo(userFriend.getUserId()) > 0) {
                    roomId = userFriend.getFriendId() + userFriend.getUserId();
                } else {
                    roomId = userFriend.getUserId() + userFriend.getFriendId();
                }
                if (ObjectUtils.isNotEmpty(relation1)) {
                    client.joinRoom(roomId);
                    client.sendEvent("joinFriendSocket",
                            new Response()
                                    .builder()
                                    .code(Rcode.OK)
                                    .msg("????????????socket??????")
                                    .data(relation1)
                                    .build());
                } else {
                    client.joinRoom(roomId);
                    client.sendEvent("joinFriendSocket",
                            new Response()
                                    .builder()
                                    .code(Rcode.OK)
                                    .msg("????????????socket??????")
                                    .data(relation2)
                                    .build());
                }
            }
        }
    }

    //?????????????????? ????????????
    @OnEvent("groupMessage")
    public void groupMessage(SocketIOClient client, AckRequest ackRequest, GroupMessageDto groupMessageDto) {
        String randomName;
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userId", groupMessageDto.getUserId());
        User user = userMapper.selectOne(userQueryWrapper);
        if (ObjectUtils.isNotEmpty(user)) {
            QueryWrapper<UserGroup> userGroupQueryWrapper = new QueryWrapper<>();
            userGroupQueryWrapper.eq("userId", groupMessageDto.getUserId()).eq("groupId", groupMessageDto.getGroupId());
            UserGroup userGroup = userGroupMapper.selectOne(userGroupQueryWrapper);
            if (ObjectUtils.isEmpty(userGroup) || groupMessageDto.getGroupId().isEmpty()) {
                client.sendEvent("groupMessage",
                        new Response()
                                .builder()
                                .code(Rcode.FAIL)
                                .msg("?????????????????????")
                                .data("")
                                .build());
                return;
            }
            if (groupMessageDto.getMessageType().equals("image")) {
                String random = RandomStringUtils.random(10, "qwertyuiopasdfghjklzxcvbnm1234567890");
                randomName = random + groupMessageDto.getUserId() + groupMessageDto.getHeight() + groupMessageDto.getWidth();
                BufferedImage bufferedImage = imageutils.readImage(groupMessageDto.getContent());
                boolean writeImageResult = imageutils.writeImage(bufferedImage, "jpg", "public/static");
            }
            groupMessageDto.setTime(new Date().getTime());
            GroupMessage groupMessage = groupMessageDto.initializeGroupMessageDto(groupMessageDto);
            boolean result = RedisUtil.setMessageCacheToRedis(groupMessage);
            if (Boolean.TRUE.equals(result)) {
                if (1 == 1) {
                    socketIOServer.getRoomOperations(groupMessage.getGroupId())
                            .sendEvent("groupMessage",
                                    new Response()
                                            .builder()
                                            .msg("")
                                            .data(groupMessage)
                                            .build());
                }
                return;
            } else {
                client.sendEvent("groupMessage",
                        new Response()
                                .builder()
                                .msg("??????????????????")
                                .code(Rcode.FAIL)
                                .data(groupMessage)
                                .build());
                return;
            }

        } else {
            client.sendEvent("groupMessage",
                    new Response()
                            .builder()
                            .msg("???????????????????????????")
                            .code(Rcode.ERROR)
                            .data("")
                            .build());
            return;
        }
    }

    //2022.10.09 ??????????????????????????????????????????
    //2022.10.11 ????????????????????????????????????????????????
    @OnEvent("friendMessage")
    public void friendMessage(SocketIOClient client, AckRequest ackRequest, FriendMessageDto friendMessageDto) {
        String roomId;
        String randomName;
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userId", friendMessageDto.getUserId());
        User user = userMapper.selectOne(userQueryWrapper);
        if (ObjectUtils.isNotEmpty(user)) {
            if (!friendMessageDto.getUserId().isEmpty() && !friendMessageDto.getFriendId().isEmpty()) {
                if (friendMessageDto.getFriendId().compareTo(friendMessageDto.getUserId()) > 0) {
                    roomId = friendMessageDto.getFriendId() + friendMessageDto.getUserId();
                } else {
                    roomId = friendMessageDto.getUserId() + friendMessageDto.getFriendId();
                }
                if (friendMessageDto.getMessageType().equals("image")) {
                    String random = RandomStringUtils.random(10, "qwertyuiopasdfghjklzxcvbnm1234567890");
                    randomName = random + friendMessageDto.getUserId() + friendMessageDto.getHeight() + friendMessageDto.getWidth();
                    BufferedImage bufferedImage = imageutils.readImage(friendMessageDto.getContent());
                    boolean writeImageResult = imageutils.writeImage(bufferedImage, "jpg", "public/static");
                }
                friendMessageDto.setTime(new Date().getTime());
                FriendMessage friendMessage = FriendMessageDto.initializeFriendMessageDto(friendMessageDto);
                //?????????????????????redis
                logger.info("??????????????????");
                boolean result = RedisUtil.setMessageCacheToRedis(friendMessage);
                if (Boolean.TRUE.equals(result)) {
//                    System.out.println("roomId:" + roomId);
                    socketIOServer.getRoomOperations(roomId)
                            .sendEvent("friendMessage",
                                    new Response()
                                            .builder()
                                            .msg("")
                                            .data(friendMessage)
                                            .build());
                    return;
                } else {
                    client.sendEvent("friendMessage",
                            new Response()
                                    .builder()
                                    .code(Rcode.FAIL)
                                    .msg("????????????")
                                    .data(friendMessage)
                                    .build());
                    return;
                }
            }
        } else {
            client.sendEvent("friendMessage",
                    new Response()
                            .builder()
                            .code(Rcode.ERROR)
                            .msg("????????????????????????")
                            .data("")
                            .build());
            return;
        }
    }

    //????????????????????????
    public void getActiveGroupUser() {
        ArrayList<String> userIdArray = new ArrayList<>();
        socketIOServer.getAllClients().forEach(clientCache -> {
            String userId = clientCache.getHandshakeData().getSingleUrlParam("userId");
            userIdArray.add(userId);
        });
        //????????????
        List<String> UserIdArr = userIdArray.stream().distinct().collect(Collectors.toList());
        HashMap<String, HashMap<String, User>> activeGroupUserGather = new HashMap<>();
        userIdArray.forEach(userId -> {
            QueryWrapper<UserGroup> userGroupQueryWrapper = new QueryWrapper<>();
            userGroupQueryWrapper.eq("userId", userId);
            List<UserGroup> userGroups = userGroupMapper.selectList(userGroupQueryWrapper);
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("userId", userId);
            User user = userMapper.selectOne(userQueryWrapper);
            userGroups.forEach(userGroup -> {
                if (ObjectUtils.isNotEmpty(userGroup) && ObjectUtils.isNotEmpty(user)) {
                    if (1 == 0) {
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        System.out.println(user);
                    }
                    HashMap<String, User> stringUserHashMap = new HashMap<>();
                    stringUserHashMap.put(user.getUserId(), user);
                    //System.out.println(stringUserHashMap);
                    activeGroupUserGather.put(userGroup.getGroupId(), stringUserHashMap);
                }
            });
        });
        if (1 == 0) {
            System.out.println("========================activeGroupUsers=====================================");
            System.out.println(activeGroupUserGather);
            System.out.println("========================activeGroupUsers=====================================");
        }
        socketIOServer.getRoomOperations(DEFAULT_ROOM).sendEvent("activeGroupUser",
                new Response<>()
                        .builder()
                        .msg("activeGroupUser")
                        .data(activeGroupUserGather)
                        .build());
    }

}
