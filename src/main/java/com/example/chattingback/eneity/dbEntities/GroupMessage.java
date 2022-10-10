package com.example.chattingback.eneity.dbEntities;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("group_message")
public class GroupMessage {

  private long _id;
  private String userId;
  private String groupId;
  private String content;

  private String messageType;

  private long time;
  public void setTime(long time) {
    this.time = time;
  }




  public long getId() {
    return _id;
  }

  public void setId(long id) {
    this._id = id;
  }


  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }


  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
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

}
