package com.hust.classroomreserve.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.hust.classroomreserve.entity.Buildings;

import java.util.List;
import java.util.Map;

@Mapper
public interface ClassroomMapper {
    void addBuilding(Buildings building);
    void deleteBuilding(Map<String, Object> param);
    void updateBuilding(Map<String, Object> param);

    // 重点修改：使用 Map 作为参数类型，支持动态参数传入
    List<Buildings> list(Map<String, Object> param);
    
    Buildings findBuildingByName(String buildName);
}