package kr.co.mgv.user.dao;

import kr.co.mgv.user.vo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface UserDao {
    User getUserById(String id);
    User getUserByEmail(String email);

    // 개인정보수정, 회원탈퇴
    void updateUser(User user);
    // 비밀번호 변경
    void updatePassword(User user);

    // 탈퇴처리
    void disabledUser(User user);

}
