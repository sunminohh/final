package kr.co.mgv.user.service;

import kr.co.mgv.user.dao.UserDao;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserDao userDao;

    public User getUserById(String id) {
        return userDao.getUserById(id);
    }

    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    // 비밀번호 변경
    public void updatePassword(String id, String newPassword) {
        User user = userDao.getUserById(id);

        user.setPassword(newPassword);
        user.setPwdUpdateDate(new Date());

        userDao.updatePassword(user);
    }

    // todo 회원 정보 수정
    public void updateUser(String id, String email, String zipcode, String address) {
        // todo 로직
        User user = userDao.getUserById(id);

        user.setEmail(email);
        user.setZipcode(zipcode);
        user.setAddress(address);
        user.setUpdateDate(new Date());

        userDao.updateUser(user);
    }

    // todo 회원탈퇴
    public void disableUser(String id, List<String> name, String roleName) {
        User user = userDao.getUserById(id);

        user.setDisabled("Y");

        List<String> roles = user.getRoleName();
        roles.remove(roleName);
        user.setRoleName(roles);

        userDao.disabledUser(user);
    }

    // 수정일자 계산
    public long getMindate(Date updateDate) {
        Date currentDate = new Date();
        long timeDifference = currentDate.getTime() - updateDate.getTime();
        long daysDifference = timeDifference / (1000 * 60 * 60 * 24);
        return daysDifference;
    }

    public long getPwdMindate(Date pwdUpdateDate) {
        Date currentDate = new Date();
        long timeDifference = currentDate.getTime() - pwdUpdateDate.getTime();
        long daysDifference = timeDifference / (1000 * 60 * 60 * 24);
        return daysDifference;
    }
}
