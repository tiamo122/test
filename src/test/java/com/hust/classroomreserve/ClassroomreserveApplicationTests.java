package com.hust.classroomreserve;
		
import com.hust.classroomreserve.entity.Buildings;
import com.hust.classroomreserve.mapper.BuildingsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

		
import java.util.List;
		
@SpringBootTest
class ClassroomreserveApplicationTests {
		
	@Autowired
	private BuildingsMapper buildingsMapper;
		
	
		}