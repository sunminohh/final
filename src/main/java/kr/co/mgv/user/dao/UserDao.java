package kr.co.mgv.user.dao;

import kr.co.mgv.user.vo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    User getUserById(String id);
    User getUserByEmail(String email);

    void updatePassword(User user);

}
