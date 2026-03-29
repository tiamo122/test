package com.hust.classroomreserve.service.serviceImpl;
import com.hust.classroomreserve.mapper.ImportCurriculumMapper;
import com.hust.classroomreserve.service.ImportCurriculumService;

import lombok.extern.slf4j.Slf4j;
import com.hust.classroomreserve.utils.ExcelData;
import jakarta.annotation.Resource;
import com.hust.classroomreserve.entity.Buildings;
import java.sql.Date;

import org.springframework.stereotype.Service;
@Slf4j
@Service
public class ImportCurriculumServiceImpl implements ImportCurriculumService {
    @Resource
    private ImportCurriculumMapper importCurriculumMapper;
    @Override
    public void changedata(ExcelData data){
        Buildings build = new Buildings();
        build.setBuildName(data.getClassPlace());
        build.setBuildSection(data.getClassSection());
        build.setBuildTime(Date.valueOf(data.getClassTime()));
        //log.info("导入的数据{}",build);
        importCurriculumMapper.insertCurriculum(build);

    }
    
}
