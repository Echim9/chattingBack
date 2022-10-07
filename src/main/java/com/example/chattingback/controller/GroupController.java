package com.example.chattingback.controller;

import com.example.chattingback.eneity.dbEntities.Group;
import com.example.chattingback.eneity.Response;
import com.example.chattingback.eneity.dbEntities.User;
import com.example.chattingback.service.imp.GroupServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("group")
public class GroupController {

    @Autowired
    private GroupServiceImp groupServiceImp;

    //创建群组
    @PostMapping("/createGroup")
    public Response creatGroup(@RequestBody Group group){
        Group newGroup = new Group();
        newGroup.setGroupName(group.getGroupName());
        Date nowTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatedTime = sdf.format(nowTime);
        newGroup.setCreateTime(formatedTime);
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

}
