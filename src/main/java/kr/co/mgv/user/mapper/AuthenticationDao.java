package kr.co.mgv.user.mapper;

import kr.co.mgv.user.vo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthenticationDao {

    void insertUser(User user);

    User getUserById(String id);

    User getUserByEmail(String email);
}
