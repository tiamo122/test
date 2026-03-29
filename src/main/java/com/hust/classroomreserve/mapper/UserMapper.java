package com.hust.classroomreserve.mapper;

import com.hust.classroomreserve.entity.Users;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    void insertUser(Users user);
    void deleteUserById(String id);
    Users getUserById(String id);
    void updateUser(Users user);
    List<Users> getUsersByRole(int role);
}
