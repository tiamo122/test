package com.hust.classroomreserve.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.sql.Date;
import java.util.List;
import com.hust.classroomreserve.entity.Buildings;
@Mapper
public interface BuildingsMapper {
    public List<Buildings> list(Date bkTime);
    public List<Buildings> findbuildings(String buildTime);
}
