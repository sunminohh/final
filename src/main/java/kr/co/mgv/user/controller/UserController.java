package kr.co.mgv.user.controller;

import kr.co.mgv.user.form.UserUpdateForm;
import kr.co.mgv.user.service.UserService;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.rmi.server.RemoteServer;
import java.time.LocalDate;
import java.util.Date;

@Controller
@Secured({"ROLE_USER", "ROLE_ADMIN"})
@RequestMapping("/user/info")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @RequestMapping({"/",""})
    public String home(@AuthenticationPrincipal User user, Model model) {
        String userId = user.getId();
        String userName = user.getName();
        String userEmail = user.getEmail();

        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        model.addAttribute("userEmail", userEmail);

        return "view/user/home";
    }

    // 이메일 인증
    @GetMapping("/auth")
    public String emailAuthForm(@AuthenticationPrincipal User user, Model model) {
        String userId = user.getId();
        String userName = user.getName();
        String userEmail = user.getEmail();

        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        model.addAttribute("userEmail", userEmail);

        return "view/user/info/auth";
    }

    @PostMapping("/auth")
    public String success() {
        return "redirect:/user/info/form";
    }

    // 회원정보 수정
    @GetMapping("/form")
    public String myMGV(@AuthenticationPrincipal User user, Model model) {
        String id = user.getId();
        String name = user.getName();
        LocalDate birth = user.getBirth();
        String email = user.getEmail();
        String zipcode = user.getZipcode();
        log.info(zipcode);
        String address = user.getAddress();
        log.info(address);
        Date updateDate = user.getUpdateDate();
        long daysDifference = userService.daysDifference(user.getUpdateDate());

        model.addAttribute("userId", id);
        model.addAttribute("userName", name);
        model.addAttribute("userBirth", birth);
        model.addAttribute("userEmail", email);
        model.addAttribute("zipcode", zipcode);
        model.addAttribute("address", address);
        model.addAttribute("updateDate", updateDate);
        model.addAttribute("daysDiff", daysDifference);

        return "view/user/info/form";
    }

    // todo 회원정보 수정
    @PostMapping("/update")
    public ResponseEntity<String> updateUser(@AuthenticationPrincipal User user, UserUpdateForm form, HttpSession session) {
        // todo 이메일 수정 할 때


        // todo 이메일 수정 안할 때
        if (!user.getEmail().equals(form.getEmail())) {
            userService.updateUser(user.getId(), form.getEmail(), form.getZipcode(), form.getAddress());
            session.invalidate();
            log.info("사용자 아이디 -> {}", user.getId());
            log.info("사용자 이메일 -> {}", form.getEmail());
            log.info("사용자 우편번호 -> {}", form.getZipcode());
            log.info("사용자 주소 -> {}", form.getAddress());
            return ResponseEntity.ok("사용가능한 이메일 주소입니다.");
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
            log.info("입력값 -> {}", form.getCheckPassword());
            log.info("새비밀번호 -> {}", form.getNewPassword());
            return ResponseEntity.ok("비밀번호가 변경되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("현재 비밀번호가 일치하지 않습니다.");
        }
    }

    @GetMapping("/disabled")
    public String disableForm(@AuthenticationPrincipal User user, Model model) {
        String id = user.getId();
        String name = user.getName();

        model.addAttribute("userId", id);
        model.addAttribute("userName", name);

        return "view/user/info/disabled";
    }

    // todo 사용자 이메일 체크
    @PostMapping("/checkEmail")
    public ResponseEntity<String> checkEmail(@AuthenticationPrincipal User user, UserUpdateForm form) {
        if (form.getEmail().equals(user.getEmail())) {
            return ResponseEntity.ok("등록된 이메일과 일치합니다.");
        } else {
            return ResponseEntity.badRequest().body("등록된 이메일 주소와 일치하지 않습니다.");
        }
    }

    // todo 회원 탈퇴
    @PostMapping("/disabled")
    public ResponseEntity<String> disabled(@AuthenticationPrincipal User user, UserUpdateForm form) {
        if (passwordEncoder.matches(form.getCheckPassword(), user.getPassword())) {
            // todo service 작성

            return ResponseEntity.ok("탈퇴처리 되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("현재 비밀번호가 일치하지 않습니다.");
        }
    }

    @GetMapping("/booking")
    public String booking() {
        return "view/user/booking/list";
    }

    @GetMapping("/ticket")
    public String ticekt() {
        return "view/user/ticket/list";
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
