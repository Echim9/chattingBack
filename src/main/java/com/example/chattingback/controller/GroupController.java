package com.example.chattingback.controller;

import com.example.chattingback.controller.socketIO.ServerRunner;
import com.example.chattingback.eneity.dbEntities.Group;
import com.example.chattingback.eneity.dbEntities.User;
import com.example.chattingback.eneity.response.Response;
import com.example.chattingback.mapper.UserMapper;
import com.example.chattingback.service.imp.GroupServiceImp;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("group")
public class GroupController {

    @Resource
    private ServerRunner serverRunner;


    @Resource
    private UserMapper userMapper;
    @Resource
    private GroupServiceImp groupServiceImp;

    @Resource
    private com.example.chattingback.mapper.GroupMessage groupMessage;

    //创建群组
    @PostMapping("/createGroup")
    public Response creatGroup(@RequestBody Group group){
        Group newGroup = new Group();
        newGroup.setGroupName(group.getGroupName());
        Date nowTime = new Date();
        newGroup.setCreateTime(nowTime.getTime());
        newGroup.setGroupId(UUID.randomUUID().toString());
        return groupServiceImp.InitGroup(newGroup);
        }

    @GetMapping("/groupUser")
    public Response getGroupUsers(@RequestParam String groupName) {
        Group group = new Group();
        group.setGroupName(groupName);
        return groupServiceImp.getGroupUsers(group);
    }

    @GetMapping("/userGroup")
    public Response getUserGroups(@RequestParam String userName){
        User user = new User();
        user.setUsername(userName);
        return groupServiceImp.getUserGroups(user);
    }

    @GetMapping("/findByName")
    public Response getGroupByName(@RequestParam String groupName){
        System.out.println("groupName" + groupName);
        Group group = new Group();
        group.setGroupName(groupName);
        return groupServiceImp.findGroupByName(group);
    }

    @GetMapping("/groupMessgaes")
    public Response getGroupMessages(@PathVariable String groupId,@PathVariable int current, @PathVariable int pageSize) {
        Response response = groupServiceImp.getGroupMessages(groupId, current, pageSize);
        return response;
    }

    @PostMapping("")
    public Response postGroups(@RequestBody String groupId){
        Response response = groupServiceImp.postGroups(groupId);
        return response;
    }
}
