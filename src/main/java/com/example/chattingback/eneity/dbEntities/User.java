package com.example.chattingback.eneity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("User")
public class User {

  private String userId;
  private String username;
  private String password;
  private String avator;
  private String tag;
  private String createTime;
  private String role;


  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  public String getAvator() {
    return avator;
  }

  public void setAvator(String avator) {
    this.avator = avator;
  }


  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }


  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }


  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

}
