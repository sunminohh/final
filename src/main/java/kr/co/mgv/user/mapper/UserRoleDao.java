package kr.co.mgv.user.mapper;

import kr.co.mgv.user.vo.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRoleDao {

    void insertUserRole(UserRole userRole);

    List<UserRole> getUserRoleByUserId(String userId);
}
