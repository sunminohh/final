package kr.co.mgv.support;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/support")
public class SupportController {

    @RequestMapping({"/", ""})
    public String home() {
        return "/view/support/home";
    }

    @GetMapping("/notice")
    public String notice() {
        return "/view/support/notice/list";
    }

    @GetMapping("/notice/detail")
    public String noticeDetail(@RequestParam(defaultValue = "1") int noticeNo) {
        return "/view/support/notice/detail";
    }

    @GetMapping("/one")
    public String one() {
        return "/view/support/one/list";
    }

    @GetMapping("/lost")
    public String lost() {
        return "/view/support/lost/list";
    }

    @GetMapping("/lost/form")
    public String lostForm() {
        return "/view/support/lost/form";
    }

    @GetMapping("/guest/form")
    public String guestForm() {
        return "/view/support/guest/form";
    }

    @GetMapping("/terms")
    public String terms() {
        return "/view/support/terms/list";
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "/view/support/privacy-policy/list";
    }
}
