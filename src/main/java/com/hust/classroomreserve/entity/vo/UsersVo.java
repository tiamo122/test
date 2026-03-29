package com.hust.classroomreserve.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersVo {
    private String id;
    private String password;
    private String name;
    private String role;
    private String token;
    
}
