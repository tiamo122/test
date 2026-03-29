package com.hust.classroomreserve.service;
import org.springframework.stereotype.Service;

import com.hust.classroomreserve.utils.ExcelData;
@Service
public interface ImportCurriculumService {
    public void changedata(ExcelData excelData);

}
