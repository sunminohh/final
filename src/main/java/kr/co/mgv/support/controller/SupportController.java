package kr.co.mgv.support.controller;

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

    @GetMapping("/faq")
    public String feq() {
        return "/view/support/faq/list";
    }

    @GetMapping("/notice")
    public String notice() {
        return "/view/support/notice/list";
    }

    @GetMapping("/notice/detail")
    public String noticeDetail(@RequestParam(defaultValue = "1") int noticeNo) {
        return "/view/support/notice/detail";
    }

    @GetMapping("/oneonone")
    public String oneonone() {
        return "/view/support/oneonone/list";
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
