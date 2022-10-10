package com.example.chattingback.eneity.dbEntities;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("friend_message")
public class FriendMessage {

  private long _id;
  private String userId;
  private String friendId;
  private String content;
  private long time;

  private String messageType;


  public long getId() {
    return _id;
  }

  public void setId(long id) {
    this._id = id;
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

}
