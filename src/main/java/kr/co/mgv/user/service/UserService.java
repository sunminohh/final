package kr.co.mgv.user.service;

import kr.co.mgv.common.dao.CommonDao;
import kr.co.mgv.common.file.FileUtils;
import kr.co.mgv.common.vo.MgvFile;
import kr.co.mgv.user.dao.UserDao;
import kr.co.mgv.user.dao.UserRoleDao;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final CommonDao commonDao;
    private final UserDao userDao;
    private final UserRoleDao userRoleDao;
    private final FileUtils fileUtils;

    private final static String USER_IMAGE_PATH = "user";

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
        User user = User.builder()
                .id(id)
                .email(email)
                .zipcode(zipcode)
                .address(address)
                .updateDate(new Date())
                .build();

        userDao.updateUser(user);
    }

    // 이미지 등록
    public String updateUploadProfile(String id, MultipartFile file) {
        // 이미지 처리
        MgvFile savedProfileImgFile = fileUtils.saveFile(USER_IMAGE_PATH, file);
        commonDao.insertMgvFile(savedProfileImgFile);
        log.info("이미지 파일명 -> {}", file);
        User user = User.builder()
                .id(id)
                .profileImg(savedProfileImgFile.getUploadPath() + File.separator + savedProfileImgFile.getStoredName())
                .build();

        user.setProfileImg(String.valueOf(savedProfileImgFile.getFileId()));
        userDao.updateUploadProfile(user);
        return user.getProfileImg();
    }

    // 이미지 삭제
    public void deleteProfileImg(String id, String imgUrl) {
        User user = userDao.getUserById(id);
        userDao.deleteProfileImage(user);
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

    private String extractFilenameFromUrl(String url) {
        // URL에서 파일명을 추출하는 로직을 구현합니다.
        // 예: "/images/user/profile/image.jpg" -> "image.jpg"
        return url.substring(url.lastIndexOf('/') + 1);
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
