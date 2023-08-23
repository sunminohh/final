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
    public void updateUser(String id, String email, String zipcode, String address) {
        // todo 로직
        User user = userDao.getUserById(id);

        user.setEmail(email);
        user.setZipcode(zipcode);
        user.setAddress(address);

        userDao.updateUser(user);
    }

    // todo 회원탈퇴

    // 수정일자 계산
    public long daysDifference(Date updateDate) {
        Date currentDate = new Date();
        long timeDifference = currentDate.getTime() - updateDate.getTime();
        long daysDifference = timeDifference / (1000 * 60 * 60 * 24);
        return daysDifference;
    }
}
