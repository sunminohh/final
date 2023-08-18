package kr.co.mgv.user.controller;

import kr.co.mgv.user.service.AuthenticationService;
import kr.co.mgv.user.service.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    @GetMapping("/auth")
    public String pwdCheckForm() {
        return "view/user/info/auth";
    }

    // 이메일 인증 후 회원정보 폼
    @GetMapping("/form")
    public String info() {

        return "view/user/info/form";
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
