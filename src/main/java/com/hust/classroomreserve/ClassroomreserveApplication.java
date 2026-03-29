package com.hust.classroomreserve;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hust.classroomreserve.mapper")
public class ClassroomreserveApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClassroomreserveApplication.class, args);
	}

}