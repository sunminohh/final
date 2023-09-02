package kr.co.mgv.user.controller;

import kr.co.mgv.user.form.UserUpdateForm;
import kr.co.mgv.user.service.MypageService;
import kr.co.mgv.user.service.UserService;
import kr.co.mgv.user.vo.Purchase;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Secured({"ROLE_USER", "ROLE_ADMIN"})
@RequestMapping("/mypage")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final MypageService mypageService;
    private final PasswordEncoder passwordEncoder;

    private String getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @RequestMapping({"/", ""})
    public String home(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);

        return "view/user/home";
    }

    // 이메일 인증
    @GetMapping("/auth")
    public String emailAuthForm(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("user", user);

        return "view/user/info/auth";
    }

    @PostMapping("/auth")
    public String success() {
        return "redirect:/mypage/form";
    }

    // 회원정보 수정
    @GetMapping("/form")
    public String myMGV(@AuthenticationPrincipal User user, Model model) {
        user = userService.getUserById(user.getId());
        long minDate = userService.getMinDate(user.getUpdateDate());
        long pwdMinDate = userService.getMinDate(user.getPwdUpdateDate());

        model.addAttribute("user", user);
        model.addAttribute("minDate", minDate);
        model.addAttribute("pwdMinDate", pwdMinDate);

        return "view/user/info/form";
    }

    // 회원정보 수정
    @PostMapping("/update")
    public ResponseEntity<String> updateUser(@AuthenticationPrincipal User user, UserUpdateForm form) {
        // 만약 이메일을 수정하지 않는 경우
        if (form.getEmail().equals(user.getEmail())) {
            userService.updateUser(user.getId(), form.getEmail(), form.getZipcode(), form.getAddress());
            return ResponseEntity.ok("이메일 수정 안함");
        }

        // 이메일을 수정하는 경우
        User checkEmail = userService.getUserByEmail(form.getEmail());

        if (checkEmail == null || checkEmail.getId().equals(user.getId())) {
            userService.updateUser(user.getId(), form.getEmail(), form.getZipcode(), form.getAddress());
            return ResponseEntity.ok("가능");
        } else {
            return ResponseEntity.badRequest().body("중복된 이메일 주소입니다.");
        }

    }

    // 비밀번호 변경
    @GetMapping("/update/password")
    public String pwdForm() {
        return "view/user/info/pwdForm";
    }

    @PostMapping("/update/password")
    public ResponseEntity<String> updatePwd(@AuthenticationPrincipal User user, UserUpdateForm form) {
        if (passwordEncoder.matches(form.getCheckPassword(), user.getPassword())) {
            userService.updatePassword(user.getId(), passwordEncoder.encode(form.getNewPassword()));
            return ResponseEntity.ok("비밀번호가 변경되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("현재 비밀번호가 일치하지 않습니다.");
        }
    }

    // 회원 탈퇴
    @GetMapping("/disabled")
    public String disableForm(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("user", user);

        return "view/user/info/disabled";
    }

    // todo 사용자 이메일 체크
    @PostMapping("/checkEmail")
    public ResponseEntity<String> checkEmail(@AuthenticationPrincipal User user, UserUpdateForm form) {
        if (form.getEmail().equals(user.getEmail())) {
            return ResponseEntity.ok(user.getEmail());
        } else {
            return ResponseEntity.badRequest().body("등록된 이메일 주소와 일치하지 않습니다.");
        }
    }

    // todo 회원 탈퇴
    @PostMapping("/disabled")
    public ResponseEntity<String> disableUser(@AuthenticationPrincipal User user, UserUpdateForm form) {
        if (passwordEncoder.matches(form.getCheckPassword(), user.getPassword())) {
            userService.disableUser(user.getId(), form.getReason());

            return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("현재 비밀번호가 일치하지 않습니다.");
        }
    }

    @GetMapping("/booking")
    public String bookinghome() {

        return "view/user/booking/list";
    }

    @PostMapping("/purchase")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> purchaseList(@RequestParam String startDate,
                                                       @RequestParam String endDate,
                                                       @RequestParam String status,
                                                       @RequestParam(name = "page", defaultValue = "1") int page) {
        String userId = getLoggedInUserId();
        log.info("loginId -> {}", userId);
        HashMap<String, Object> purchases = mypageService.getPurchaseByUserId(userId, startDate, endDate, status, page);
        log.info("page -> {}", page);
        log.info("startDate -> {}", startDate);
        log.info("endDate -> {}", endDate);
        log.info("status -> {}", status);
        return ResponseEntity.ok(purchases);
    }

    @GetMapping("/moviestory")
    public String moviestory() {
        return "view/user/moviestory/list";
    }

    @GetMapping("/inquiry")
    public String inquiry() {

        return "view/user/inquiry/list";
    }

}
