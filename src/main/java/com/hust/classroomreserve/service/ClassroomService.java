package com.hust.classroomreserve.service;

import com.hust.classroomreserve.entity.Buildings;
import com.hust.classroomreserve.entity.Booking;
import com.hust.classroomreserve.entity.Users;
import com.hust.classroomreserve.entity.vo.UsersVo;

import java.util.List;
import java.util.Map;

public interface ClassroomService {
    public void addBuilding(Buildings building);
    void deleteBuilding(String buildName, String buildTime, String buildSection);
    public void updateBuilding(Buildings building);
 
    List<Buildings> listBuildings(String buildName, String buildTime, String buildSection, int page, int pageSize);

}