package kr.co.mgv.user.service;

import kr.co.mgv.user.dao.UserDao;
import kr.co.mgv.user.dao.UserRoleDao;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserDao userDao;
    private final UserRoleDao userRoleDao;

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

    // 회원 정보 수정
    public void updateUser(String id, String email, String zipcode, String address) {
        // todo 로직
        User user = userDao.getUserById(id);

        user.setEmail(email);
        user.setZipcode(zipcode);
        user.setAddress(address);
        user.setUpdateDate(new Date());

        userDao.updateUser(user);
    }

    // 회원탈퇴
    public void disableUser(String id, String reason) {
        User user = userDao.getUserById(id);

        // 사용자 정보 수정
        user.setDisabled("Y");
        user.setReason(reason);
        user.setUpdateDate(new Date());

        userDao.disabledUser(user);

        // 보유권한 변경
        Map<String, Object> param = new HashMap<>();
        param.put("userId", user.getId());
        userRoleDao.updateUserRole(param);

    }

    // 수정일자 계산
    public long getMinDate(Date updateDate) {
        Date currentDate = new Date();
        long timeDifference = currentDate.getTime() - updateDate.getTime();
        long daysDifference = timeDifference / (1000 * 60 * 60 * 24);
        return daysDifference;
    }

    public long getPwdMinDate(Date pwdUpdateDate) {
        Date currentDate = new Date();
        long timeDifference = currentDate.getTime() - pwdUpdateDate.getTime();
        long daysDifference = timeDifference / (1000 * 60 * 60 * 24);
        return daysDifference;
    }


}
