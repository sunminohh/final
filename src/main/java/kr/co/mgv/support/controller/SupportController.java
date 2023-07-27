package kr.co.mgv.support.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author SonSangGi
 * @created 2023/07/27
 * Copyright © 2023 mqnic. All rights reserved.
 */
@Controller
@RequestMapping("/support")
public class SupportController {

    @RequestMapping({"/", ""})
    public String home() {
        return "/view/support/home";
    }

}
