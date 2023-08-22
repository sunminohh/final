package kr.co.mgv.user.service;

import kr.co.mgv.user.dao.UserDao;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserDao userDao;

    // 비밀번호 변경
    public void updatePassword(String id, String newPassword) {
        User user = userDao.getUserById(id);
        user.setPassword(newPassword);
        userDao.updatePassword(user);
    }
}
