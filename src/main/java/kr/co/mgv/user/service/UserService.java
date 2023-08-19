package kr.co.mgv.user.service;

import kr.co.mgv.user.dao.AuthenticationDao;
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
    private final AuthenticationDao authenticationDao;

    public User getUserByEmail(String email) {
        return authenticationDao.getUserByEmail(email);
    }
}
