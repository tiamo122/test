package com.hust.classroomreserve.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hust.classroomreserve.common.Result;
import com.hust.classroomreserve.entity.Buildings;
import com.hust.classroomreserve.service.ClassroomService;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j  
@RestController
@RequestMapping("/buildings")
public class ClassroomController {
    private static final Logger log = LoggerFactory.getLogger(ClassroomController.class);

    @Autowired
    private ClassroomService classroomService;

    @PostMapping("/add")
    public Result addBuilding(@RequestBody List<Buildings> buildings) {
        try {
            for (Buildings building : buildings) {
                classroomService.addBuilding(building);
            }
            log.info("Buildings added successfully: {}", buildings);
            return Result.success("教室添加成功");
        } catch (Exception e) {
            log.error("Failed to add buildings: {}", buildings, e);
            return Result.error("教室添加失败");
        }
    }


    @DeleteMapping("/delete/{buildName}")
    public Result deleteBuilding(
            @PathVariable String buildName,
            @RequestParam(value = "buildTime", required = false) String buildTime,
            @RequestParam(value = "buildSection", required = false) String buildSection) {
        try {
            classroomService.deleteBuilding(buildName, buildTime, buildSection);
            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("Failed to delete building", e);
            return Result.error("删除失败");
        }
    }

    @PostMapping("/update")
    public Result updateBuilding(@RequestBody Buildings building) {
        try {
            classroomService.updateBuilding(building);
            log.info("Building updated successfully: {}", building);
            return Result.success("教室状态更新成功");
        } catch (Exception e) {
            log.error("Failed to update building: {}", building, e);
            return Result.error("教室状态更新失败");
        }
    }

    @GetMapping
    public Result listBuildings(
            @RequestParam(value = "buildName", defaultValue = "") String buildName,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "buildTime", required = false) String buildTime,
            @RequestParam(value = "buildSection", required = false) String buildSection) {
        try {
            // 启用分页
            PageHelper.startPage(page, pageSize);
            List<Buildings> buildings = classroomService.listBuildings(buildName, buildTime, buildSection, page, pageSize);

            // 包装分页结果
            PageInfo<Buildings> pageInfo = new PageInfo<>(buildings);

            // 构造返回结构
            Map<String, Object> result = new HashMap<>();
            result.put("rows", pageInfo.getList());
            result.put("total", pageInfo.getTotal());

            log.info("Listed buildings successfully: size={}", pageInfo.getTotal());
            return Result.success(result);
        } catch (Exception e) {
            log.error("Failed to list buildings", e);
            return Result.error("加载教室数据失败");
        }
    }
}