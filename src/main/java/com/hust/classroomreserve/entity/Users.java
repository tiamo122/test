package com.hust.classroomreserve.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    private String id;
    private String name;
    private String password;
    private String role;
    private String classname;
    private String email;
    private String posts;
}
