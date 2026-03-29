package com.hust.classroomreserve.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.hust.classroomreserve.entity.Booking;
@Mapper
public interface  BookingMapper {
    
    List<Booking> findbooking(String bkTime,String bkBdState);
    List<Booking> bookingrecord(String bkPsonId);
    
}
