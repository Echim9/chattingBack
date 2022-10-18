package com.example.chattingback.eneity.response;

import com.example.chattingback.eneity.dbEntities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * @author Echim9
 * @date 2022/10/12 01:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMesRes<T>  {

    private T messageArr;

    private ArrayList<User> userArr;

}
