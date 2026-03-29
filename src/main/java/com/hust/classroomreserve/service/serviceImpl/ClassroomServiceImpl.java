package com.hust.classroomreserve.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hust.classroomreserve.mapper.ClassroomMapper;
import com.hust.classroomreserve.service.ClassroomService;
import com.hust.classroomreserve.entity.Buildings;
import com.hust.classroomreserve.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("ClassroomService")
public class ClassroomServiceImpl implements ClassroomService {
    private static final Logger log = LoggerFactory.getLogger(ClassroomServiceImpl.class);

    @Autowired
    private ClassroomMapper classroomMapper;

    @Override
    public void addBuilding(Buildings building) {
        try {
            classroomMapper.addBuilding(building);
            log.info("Building added successfully: {}", building);
        } catch (Exception e) {
            log.error("Failed to add building: {}", building, e);
            throw e;
        }
    }

    @Override
    public void deleteBuilding(String buildName, String buildTime, String buildSection) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("buildName", buildName);
            param.put("buildTime", buildTime);
            param.put("buildSection", buildSection);
            classroomMapper.deleteBuilding(param);
            log.info("Building deleted successfully: {}", buildName);
        } catch (Exception e) {
            log.error("Failed to delete building", e);
            throw e;
        }
    }

    @Override
    public void updateBuilding(Buildings building) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("buildName", building.getBuildName());
            param.put("buildTime", building.getBuildTime());
            param.put("buildSection", building.getBuildSection());
            param.put("buildState", building.getBuildState());
            param.put("buildCapacity", building.getBuildCapacity());
            classroomMapper.updateBuilding(param);
            log.info("Building updated successfully: {}", building);
        } catch (Exception e) {
            log.error("Failed to update building: {}", building, e);
            throw e;
        }
    }

    @Override
    public List<Buildings> listBuildings(String buildName, String buildTime, String buildSection, int page, int pageSize) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("buildName", buildName);
            param.put("buildTime", buildTime);
            param.put("buildSection", buildSection);
            List<Buildings> buildings = classroomMapper.list(param);
            log.info("Listed buildings successfully: size={}", buildings.size());
            return buildings;
        } catch (Exception e) {
            log.error("Failed to list buildings", e);
            throw e;
        }
    }
}