package kr.co.mgv.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    @RequestMapping({"/", ""})
    public String home() {
        return "view/user/home";
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

    @GetMapping("/myinfo")
    public String myinfo() {
        return "view/user/myinfo/form";
    }

    // 나의 정보
    @GetMapping("/myinfo/pwdform")
    public String pwdform() {
        return "view/user/myinfo/pwdform";
    }
}
