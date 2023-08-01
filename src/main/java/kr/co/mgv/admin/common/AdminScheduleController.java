package kr.co.mgv.admin.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/schedule")
public class AdminScheduleController {

    @GetMapping("/list")
    public String list() {
        return "/view/admin/schedule/list";
    }
    
    @GetMapping("/register")
    public String form() {
        return "/view/admin/schedule/form";
    }
}
