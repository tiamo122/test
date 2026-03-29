package com.hust.classroomreserve.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.hust.classroomreserve.entity.Buildings;
@Mapper
public interface ImportCurriculumMapper {
    public void insertCurriculum(Buildings build);
    
}
