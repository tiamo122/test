package com.hust.classroomreserve.controller;
import java.awt.print.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hust.classroomreserve.common.Result;
import com.hust.classroomreserve.entity.Booking;
import com.hust.classroomreserve.entity.Buildings;
import com.hust.classroomreserve.entity.Users;
import com.hust.classroomreserve.service.SuperAdminService;
import java.util.List;
import com.hust.classroomreserve.mapper.BookingMapper;
import com.hust.classroomreserve.mapper.BuildingsMapper;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
public class BookingController {
    @Autowired
    private BookingMapper bookingMapper;
    @Autowired
    private BuildingsMapper buildingsMapper;
    @GetMapping("/booking")
    //条件分页查询
    public PageInfo<Booking> findbooking(@RequestParam(value="pageNow",defaultValue="1") Integer pageNow, 
    @RequestParam(value="pageSize",defaultValue="10") Integer pageSize, 
    @RequestParam(value="bkTime",required=false) @DateTimeFormat(pattern = "yyyy-MM-dd") String bkTime,
    @RequestParam(value="bkBdState",required=false) String bkBdState) {
        //log.info("分页查询,当前页码{},每页记录数{},查询条件{}",pageNow,pageSize,bkTime);
        PageHelper.startPage(pageNow, pageSize);
        List<Booking> list = bookingMapper.findbooking(bkTime,bkBdState); 
        PageInfo<Booking> pageInfo = new PageInfo<>(list);
        //log.info("total={}, pageSize={}", pageInfo.getTotal(), pageInfo.getPageSize());
        //log.info("分页结果: pageinfo={}", pageInfo);
        return pageInfo;    
    }
    @GetMapping("/building")
    //条件分页查询
    public PageInfo<Buildings> findbuildings(@RequestParam(value="pageNow",defaultValue="1") Integer pageNow, 
    @RequestParam(value="pageSize",defaultValue="10") Integer pageSize, 
    @RequestParam(value="buildTime") @DateTimeFormat(pattern = "yyyy-MM-dd") String buildTime) {
        //log.info("分页查询,当前页码{},每页记录数{},查询条件{}",pageNow,pageSize,buildTime);
        PageHelper.startPage(pageNow, pageSize);
        List<Buildings> list = buildingsMapper.findbuildings(buildTime); 
        PageInfo<Buildings> pageInfo = new PageInfo<>(list);
        //log.info("total={}, pageSize={}", pageInfo.getTotal(), pageInfo.getPageSize());
        //log.info("分页结果: pageinfo={}", pageInfo);
        return pageInfo;    
    }
    @GetMapping("/bookingrecord")
    //条件分页查询
    public PageInfo<Booking> bookingrecord(@RequestParam(value="pageNow",defaultValue="1") Integer pageNow, 
    @RequestParam(value="pageSize",defaultValue="10") Integer pageSize, 
    @RequestParam(value="bkPsonId")  String bkPsonId) {
        //log.info("分页查询,当前页码{},每页记录数{},查询条件{}",pageNow,pageSize,bkPsonId);
        List<Booking> list = bookingMapper.bookingrecord(bkPsonId); 
        PageInfo<Booking> pageInfo = new PageInfo<>(list);
        //log.info("total={}, pageSize={}", pageInfo.getTotal(), pageInfo.getPageSize());
        //log.info("分页结果: pageinfo={}", pageInfo);
        return pageInfo;    
    }
}   
