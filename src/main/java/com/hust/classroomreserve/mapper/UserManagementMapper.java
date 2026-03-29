package com.hust.classroomreserve.mapper;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.hust.classroomreserve.entity.Users;
@Mapper
public interface  UserManagementMapper {

    public List<Users> usermanagement(Users user);
    public void deleteusers(Users user);
    public void editusers(Users user);
}
