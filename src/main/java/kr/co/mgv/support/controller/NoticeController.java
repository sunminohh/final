package kr.co.mgv.support.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Controller
@RequestMapping("/support/notice")
@RequiredArgsConstructor
@ToString
public class NoticeController {
	
    @GetMapping
    public String notice() {
        return "/view/support/notice/list";
    }

    @GetMapping("/detail")
    public String noticeDetail(@RequestParam(defaultValue = "1") int noticeNo) {
        return "/view/support/notice/detail";
    }
}
