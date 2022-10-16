package com.example.chattingback.eneity.dbEntities;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_map")
public class UserFriend {
  @TableId
  private long _id;
  private String friendId;

  private String userId;


  public long getId() {
    return _id;
  }

  public void setId(long id) {
    this._id = id;
  }


  public String getFriendId() {
    return friendId;
  }

  public void setFriendId(String friendId) {
    this.friendId = friendId;
  }


  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

}
