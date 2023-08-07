package kr.co.mgv.user.mapper;

import kr.co.mgv.user.vo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthenticationDao {

    // 신규 사용자 등록
    void insertUser(User user);

    // 아이디 사용자 정보 조회
    User getUserById(String id);

    // 이메일로 중복 확인
    User getUserByEmail(String email);
}
