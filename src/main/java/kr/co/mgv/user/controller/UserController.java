package kr.co.mgv.user.controller;

import kr.co.mgv.user.service.AuthenticationService;
import kr.co.mgv.user.service.EmailServiceImpl;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@Secured("ROLE_USER")
@RequestMapping("/user/info")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final AuthenticationService authenticationService;
    private final EmailServiceImpl emailService;

    @RequestMapping({"/",""})
    public String home() {
        return "view/user/home";
    }

    // 이메일 인증
    // 로그인 된 사용자의 email을 html 에 입력
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

    // 이메일 인증 후 회원정보 폼
    @GetMapping("/form")
    public String myMGV() {
        return "view/user/info/form";
    }

    @PostMapping("/mail")
    @ResponseBody
    public ResponseEntity<String> mailConfirm(@RequestParam("email") String email, HttpSession session) {
        try {
            String code = emailService.sendSimpleMessage(email);
            log.info("인증번호 -> {}", code);

            // 생성한 인증 코드를 세션에 저장
            session.setAttribute("emailConfirmCode", String.valueOf(code));
            return ResponseEntity.ok().body("success");
        } catch (Exception e) {
            log.error("Error sending email", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메일 전송 중 오류가 발생했습니다.");
        }
    }

    // 인증번호 비교
    @PostMapping("/check")
    @ResponseBody
    public ResponseEntity<String> getSessionAuthCode(@RequestParam("code") String userCode, HttpSession session) {
        String savedCode = (String) session.getAttribute("emailConfirmCode");
        if (savedCode == null) {
            log.error("세션 인증코드 null");
            return ResponseEntity.badRequest().body("SESSION_CODE_NULL");
        } else if (userCode.isBlank()) {
            log.error("USER_CODE_NULL");
            return ResponseEntity.ok().body("USER_CODE_NULL");
        } else if (!savedCode.equals(userCode)) {
            log.error("인증번호 불일치");
            return ResponseEntity.ok().body("인증실패");
        } else {
            log.info("인증성공");
            return ResponseEntity.ok().body("인증성공");
        }
    }



    // 비밀번호 수정 -> 비밀번호 체크 폼
    @GetMapping("/pwdcheck")
    public String pwdCheck() {
        return "view/user/info/pwd-check";
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
