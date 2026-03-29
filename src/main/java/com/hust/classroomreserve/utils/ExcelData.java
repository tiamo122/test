package com.hust.classroomreserve.utils;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;

import lombok.Data;
@Data
public class ExcelData {
    @ExcelProperty("课程名称")
    private String courseName;
    @ExcelProperty("上课时间")
    @DateTimeFormat("yyyy-MM-dd")
    private String classTime;
    @ExcelProperty("上课地点")
    private String classPlace;
    @ExcelProperty("上课教师")
    private String classTeacher;
    @ExcelProperty("上课班级")
    private String classClass;
    @ExcelProperty("上课节次")
    private String classSection;
    
}
