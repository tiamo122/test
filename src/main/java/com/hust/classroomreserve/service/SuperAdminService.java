package com.hust.classroomreserve.service;

import java.sql.Date;

import com.hust.classroomreserve.entity.Booking;
import com.hust.classroomreserve.entity.Users;
import com.hust.classroomreserve.entity.vo.UsersVo;

public interface SuperAdminService {
    public void booking(Booking booking);
    public void changebdstate(Booking booking);
    public UsersVo login(Users users);
    public void bookingrecord(Booking booking);
    public void changebdstate0(Booking booking);


    
}
