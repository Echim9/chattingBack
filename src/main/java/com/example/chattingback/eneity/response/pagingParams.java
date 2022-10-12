package com.example.chattingback.eneity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Echim9
 * @date 2022/10/12 12:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class pagingParams {

        private String groupId;

        private String userId;

        private String friendId;

        private int current;

        private int pageSize;
    }

