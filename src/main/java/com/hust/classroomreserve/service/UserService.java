package com.hust.classroomreserve.service;

import com.hust.classroomreserve.entity.Users;

import java.util.List;

public interface UserService {
    void addUser(Users user);
    void deleteUser(String id);
    Users getUser(String id);
    void updateUser(Users user);
    List<Users> getUsersByRole(int role);
}
