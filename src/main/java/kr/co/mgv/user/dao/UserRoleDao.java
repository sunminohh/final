package kr.co.mgv.user.dao;

import kr.co.mgv.user.vo.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRoleDao {

    // 사용자 권한 정보 저장
    void insertUserRole(UserRole userRole);

    // 저장된 사용자 아이디에 해당하는 사용자의 모든 보유 권한 조회
    List<UserRole> getUserRoleByUserId(String userId);
}
