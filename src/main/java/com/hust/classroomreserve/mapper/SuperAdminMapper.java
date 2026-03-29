package com.hust.classroomreserve.mapper;
import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.hust.classroomreserve.entity.Booking;

import com.hust.classroomreserve.entity.Users;
@Mapper
public interface SuperAdminMapper  {
    public void booking(Booking booking);
    public void changebdstate(Booking booking);
    public Users login(Users user);
    public void bookingrecord(Booking booking);
    public void changebdstate0(Booking booking);
    public void bookingconfirm(Booking booking);
    public void changebdstate1(Booking booking);
    
    public void refused(Booking booking);
}
