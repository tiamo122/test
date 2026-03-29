package com.hust.classroomreserve.controller;

import java.sql.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hust.classroomreserve.common.Result;
import com.hust.classroomreserve.entity.Booking;
import com.hust.classroomreserve.entity.Users;
import com.hust.classroomreserve.entity.vo.UsersVo;
import com.hust.classroomreserve.mapper.SuperAdminMapper;
import com.hust.classroomreserve.service.SuperAdminService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/superadmin")
public class SuperAdminController {

    private static final Logger logger = LoggerFactory.getLogger(SuperAdminController.class);

    @Autowired
    private SuperAdminService superAdminService;
    @Autowired
    private SuperAdminMapper superAdminMapper;

    @PostMapping("/booking")//预约
    public void booking(@RequestBody Booking booking) {
        //System.out.println(booking);
        superAdminService.booking(booking);
        superAdminService.changebdstate(booking);
    }

    @PostMapping("/login")//登录
    public Result login(@RequestBody Users users) {

            UsersVo u = superAdminService.login(users);
            if(u==null){
                return Result.error("用户名或密码错误");
            }
            
            //System.out.println(Result.success(u));
            return Result.success(u);
    }
    @PostMapping("/bookingrecordmethods")//查询预约记录
    public void bookingrecord(@RequestBody Booking booking) {
        superAdminService.bookingrecord(booking);//撤销，修改booking状态
        superAdminService.changebdstate0(booking);//修改buidings状态
    }
    @PostMapping("/bookingconfirm")
    public void bookingconfirm(@RequestBody Booking booking) {
            superAdminMapper.bookingconfirm(booking);
            //System.out.println("bookingconfirm{}"+booking);
            if ("已审核".equals(booking.getBkBdState())) { 
                superAdminMapper.changebdstate1(booking);
            }else{
                superAdminMapper.changebdstate0(booking);
                superAdminMapper.refused(booking);
            }
        }
    }