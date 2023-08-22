package kr.co.mgv.user.service;

import kr.co.mgv.user.dao.UserDao;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserDao userDao;

    // 비밀번호 변경
    public void updatePassword(String id, String newPassword) {
        User user = userDao.getUserById(id);

        user.setPassword(newPassword);
        user.setUpdateDate(new Date());

        userDao.updatePassword(user);
    }

    // todo 회원 정보 수정
    public void updateUser(User user) {
        // todo 로직

        userDao.updateUser(user);
    }

    // todo 회원탈퇴

}
