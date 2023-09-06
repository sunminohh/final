package kr.co.mgv.event.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin/event")
public class AdminEventController {

    @GetMapping("/list")
    public String home() {
        return "view/admin/event/list";
    }

    @GetMapping("/insertform")
    public String eventInsertForm() {
    	return "view/admin/event/insertform";
    }
    
    
    
}