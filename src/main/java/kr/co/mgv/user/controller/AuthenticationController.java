package kr.co.mgv.user.controller;

import kr.co.mgv.user.form.UserJoinForm;
import kr.co.mgv.user.service.AuthenticationService;
import kr.co.mgv.user.service.EmailServiceImpl;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user/auth")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final EmailServiceImpl emailService;
    private final AuthenticationManager authenticationManager;


    // 회원가입
    @GetMapping("/form")
    public String createForm(Model model) {
        UserJoinForm form = new UserJoinForm();
        model.addAttribute("userJoinForm", form);

        return "/view/auth/form";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute("userJoinForm") UserJoinForm form, SessionStatus sessionStatus, RedirectAttributes redirectAttributes) {
        // Service -> DAO (user, role)
        User user = new User();
        user.setId(form.getId());
        user.setName(form.getName());
        user.setEmail(form.getEmail());
        user.setBirth(form.getBirth());
        user.setPassword(form.getPassword());

        authenticationService.createUser(user);

        UserDetails userDetails = authenticationService.loadUserByUsername(form.getId());
        redirectAttributes.addFlashAttribute("user", userDetails);

        sessionStatus.setComplete();
        return "redirect:/user/auth/registered";
    }

    @GetMapping("/registered")
    public String registered() {
        return "/view/auth/registered";
    }

    @ResponseBody
    @GetMapping("/checkId")
    public ResponseEntity<Boolean> checkId(@RequestParam String id) {
        return ResponseEntity.ok(authenticationService.loadUserByUsername(id) != null);
    }

    @ResponseBody
    @GetMapping("/checkEmail")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        return ResponseEntity.ok(authenticationService.getUserByEmail(email) != null);
    }

    // 로그인
    @ResponseBody
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> login(HttpSession session, @RequestBody User user) { //
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getId(), user.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            User userPrincipal = (User) auth.getPrincipal(); //
            log.info("User [{}] logged in.", userPrincipal.getUsername());
            return ResponseEntity.ok(userPrincipal); //
        } catch (BadCredentialsException e) {
            log.warn("User [{}] failed login: {}", user.getUsername(), e.getLocalizedMessage());
            return ResponseEntity
                    .badRequest()
                    .body(e.getLocalizedMessage()); //
        }
    }

    // 이메일 인증
    @PostMapping("/mail")
    @ResponseBody
    public ResponseEntity<String> mailConfirm(@RequestParam("email") String email, HttpSession session) throws Exception {
        String code = emailService.sendSimpleMessage(email);
        log.info("인증코드 -> {}", code);

        // 생성한 인증 코드를 세션에 저장
        session.setAttribute("emailConfirmCode", String.valueOf(code));
        return ResponseEntity.ok().body("success");
    }

    // 인증번호 비교
    @PostMapping("/check")
    @ResponseBody
    public ResponseEntity<String> getSessionAuthCode(@RequestParam("code") String userCode, HttpSession session) {
        String savedCode = (String) session.getAttribute("emailConfirmCode");
        if (savedCode == null) {
            log.error("세션 인증코드 null");
            return ResponseEntity.badRequest().body("SESSION_CODE_NULL");
        } else if (userCode == null) {
            log.error("사용자 인증코드 입력하지 않음");
            return ResponseEntity.badRequest().body("USER_CODE_NULL");
        } else if (savedCode.equals(userCode)) {
            log.info("인증코드 일치");
            return ResponseEntity.ok().body("인증성공");
        } else {
            log.error("인증코드 불일치");
            return ResponseEntity.badRequest().body("AUTHENTICATION_FAILED");
        }
    }
}
