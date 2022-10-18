package com.example.chattingback.eneity.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Echim9
 * @date 2022/10/18 14:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDtoNull {
    private String groupId;

    private String userId;// 群主id

    private String groupName;

    private String notice;

    private long createTime;
}
