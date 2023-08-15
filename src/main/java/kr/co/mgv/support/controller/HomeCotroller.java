package kr.co.mgv.support.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/support")
@RequiredArgsConstructor
public class HomeCotroller {
	

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
