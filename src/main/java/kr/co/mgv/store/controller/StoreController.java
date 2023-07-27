package kr.co.mgv.store.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/store")
public class StoreController {

    @RequestMapping({"/", ""})
    public String home() {
        return "/view/store/home";
    }

}
