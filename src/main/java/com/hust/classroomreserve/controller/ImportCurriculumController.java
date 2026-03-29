package com.hust.classroomreserve.controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.hust.classroomreserve.utils.ExcelData;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import com.hust.classroomreserve.common.Result;
import com.hust.classroomreserve.mapper.ImportCurriculumMapper;
import com.hust.classroomreserve.service.ImportCurriculumService;
@Slf4j  
@RestController
    public class ImportCurriculumController {
        @Autowired
        private ImportCurriculumMapper importCurriculumMapper;
        @Autowired
        private ImportCurriculumService importCurriculumService;

        @PostMapping("/importcurriculum")
        public Result importCurriculum(@RequestParam("file") MultipartFile file) {
            try {
                EasyExcel.read(file.getInputStream(), ExcelData.class,
                    new PageReadListener<ExcelData>(dataList -> {
                        for (ExcelData data : dataList) {
                            log.info("读取到数据: {}", data); // 改用日志输出
                            importCurriculumService.changedata(data);
                        }
                    })).sheet().doRead();
                return Result.success(); // 直接返回成功
            } catch (IOException e) {
                log.error("文件读取失败", e); // 替换为日志记录
                return Result.error("文件解析失败"); // 补充返回语句
            }
        }
                }