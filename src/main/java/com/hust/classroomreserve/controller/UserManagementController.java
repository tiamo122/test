package com.hust.classroomreserve.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hust.classroomreserve.mapper.UserManagementMapper;

import lombok.extern.slf4j.Slf4j;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hust.classroomreserve.entity.Booking;
import com.hust.classroomreserve.entity.Users;
@Slf4j
@RestController
@RequestMapping("/usermanagement")
public class UserManagementController {
    @Autowired
    private UserManagementMapper userManagementMapper;
    @PostMapping("/findusers")
    //条件分页查询
    public PageInfo<Users> usermanagement(@RequestParam(value="pageNow",defaultValue="1") Integer pageNow, 
    @RequestParam(value="pageSize",defaultValue="10") Integer pageSize, 
    @RequestBody Users users) {
        PageHelper.startPage(pageNow, pageSize);
        log.info("分页查询,当前页码{},每页记录数{},查询条件{}",pageNow,pageSize,users);
        List<Users> list =userManagementMapper.usermanagement(users); 
        //log.info("Users{}",list);
        PageInfo<Users> pageInfo = new PageInfo<>(list);
        //log.info("total={}, pageSize={}", pageInfo.getTotal(), pageInfo.getPageSize());
        //log.info("分页结果: pageinfo={}", pageInfo);
        return pageInfo;    
    }
    @PostMapping("/deleteusers")
    public void deleteUsers(@RequestBody Users users){
        userManagementMapper.deleteusers(users);
    }
    @PostMapping("/editusers")
    public void editUsers(@RequestBody Users users){
        userManagementMapper.editusers(users);
    }
}
