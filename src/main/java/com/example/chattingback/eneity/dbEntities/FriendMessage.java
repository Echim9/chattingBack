package com.example.chattingback.eneity.dbEntities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("friend_message")
public class FriendMessage {


  @TableId(type = IdType.AUTO)
  private long _id;
  private String userId;
  private String friendId;
  private String content;
  private long time;

  private String messageType;

  public long get_id() {
    return _id;
  }

  public void set_id(long _id) {
    this._id = _id;
  }

  public String getMessageType() {
    return messageType;
  }

  public void setMessageType(String messageType) {
    this.messageType = messageType;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }


  public String getFriendId() {
    return friendId;
  }

  public void setFriendId(String friendId) {
    this.friendId = friendId;
  }


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public static FriendMessage defaultMessage(User user, User friend){
    FriendMessage friendMessage = new FriendMessage();
    friendMessage.setUserId(user.getUserId());
    friendMessage.setFriendId(friend.getUserId());
    friendMessage.setMessageType("text");
    friendMessage.setContent(user.getUsername() + "成功添加" + friend.getUsername() + "为好友");
    friendMessage.setTime(new Date().getTime());
    return friendMessage;
  }
}
