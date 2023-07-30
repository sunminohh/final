package kr.co.mgv.store.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/store")
public class StoreController {

    @GetMapping({"/", ""})
    public String home() {
        return "/view/store/home";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam(defaultValue = "1") String storeNo) {
        return "/view/store/detail";
    }

}
