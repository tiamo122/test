package com.hust.classroomreserve.controller;

import com.hust.classroomreserve.entity.Users;
import com.hust.classroomreserve.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public void addUser(@RequestBody Users user) {
        if (user.getRole() == "1") {
            userService.addUser(user);
        } else {
            throw new RuntimeException("只有管理员可以添加用户");
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable String id) {
        Users user = userService.getUser(id);
        if (user != null && user.getRole() == "1") {
            userService.deleteUser(id);
        } else {
            throw new RuntimeException("只有管理员可以删除用户");
        }
    }

    @GetMapping("/get/{id}")
    public Users getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    @PutMapping("/update")
    public void updateUser(@RequestBody Users user) {
        if (user.getRole() == "1") {
            userService.updateUser(user);
        } else {
            throw new RuntimeException("只有管理员可以更新用户");
        }
    }
}
