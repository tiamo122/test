package com.hust.classroomreserve.service.serviceImpl;

import com.hust.classroomreserve.entity.Users;
import com.hust.classroomreserve.mapper.UserMapper;
import com.hust.classroomreserve.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// src/main/java/com/hust/classroomreserve/service/serviceImpl/UserServiceImpl.java
@Slf4j
@Service("UserService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void addUser(Users user) {
        userMapper.insertUser(user);
    }

    @Override
    public void deleteUser(String id) {
        userMapper.deleteUserById(id);
    }

    @Override
    public Users getUser(String id) {
        return userMapper.getUserById(id);
    }

    @Override
    public void updateUser(Users user) {
        userMapper.updateUser(user);
    }
    @Override
    public List<Users> getUsersByRole(int role) {
        return userMapper.getUsersByRole(role);
    }
}

