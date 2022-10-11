package com.example.chattingback.eneity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Echim9
 * @date 2022/10/11 14:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupRes <T>{

    private String groupId;

    private T data;
}
/**
 *
 * public class UserRes <T>{
 *
 *     private String userId;
 *
 *     private T data;
 * }
 *public class GroupRes <T>{
 *  *
 *  *     private String groupId;
 *  *
 *  *     private T data;
 *  * }
 *
 *  arrlist<GroupRes>
 */
