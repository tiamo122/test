package com.hust.classroomreserve.service.serviceImpl;

import org.springframework.stereotype.Service;
import com.hust.classroomreserve.mapper.SuperAdminMapper;
import java.util.HashMap;
import com.hust.classroomreserve.service.SuperAdminService;
import com.hust.classroomreserve.entity.Booking;
import com.hust.classroomreserve.entity.Users;
import com.hust.classroomreserve.entity.vo.UsersVo;
import jakarta.annotation.Resource;
import com.hust.classroomreserve.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;


import java.util.List;
import java.util.Map;
@Slf4j
@Service("SuperAdminService")
public class SuperAdminServiceImpl  implements SuperAdminService {
    @Resource
    private SuperAdminMapper superAdminMapper;
    @Override
    public void booking(Booking booking){
        superAdminMapper.booking(booking);
    }
    @Override
    public UsersVo login(Users users){
        Users user = superAdminMapper.login(users);
        if(user == null){
            log.info("用户名或密码错误");
            return null;
        }
        UsersVo userVo = new UsersVo();
        userVo.setId(user.getId());
        userVo.setPassword(user.getPassword());
        userVo.setName(user.getName());
        userVo.setRole(user.getRole());
        userVo.setToken(generateToken(user));
        
        return userVo;
    }
    @Override
    public void changebdstate(Booking booking){
        superAdminMapper.changebdstate(booking);
    }
    @Override
    public void bookingrecord(Booking booking){
        superAdminMapper.bookingrecord(booking);
    }
    @Override
    public void changebdstate0(Booking booking){
        superAdminMapper.changebdstate0(booking);
    }
    private String generateToken(Users user) {
        Map<String, String> useMap = new HashMap<>();
        useMap.put("id", String.valueOf(user.getId()));
        useMap.put("password", user.getPassword());
        useMap.put("name", user.getName());
        return JwtUtils.generateToken(useMap);
    }

}
