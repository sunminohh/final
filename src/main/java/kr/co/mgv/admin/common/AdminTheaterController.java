package kr.co.mgv.admin.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/theater")
public class AdminTheaterController {

    @GetMapping("/list")
    public String list() {
        return "/view/admin/theater/list";
    }
    
    @GetMapping("/register")
    public String form() {
        return "/view/admin/theater/schedule/form";
    }
}
