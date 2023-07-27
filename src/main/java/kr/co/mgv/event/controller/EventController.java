package kr.co.mgv.event.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/event")
public class EventController {

    @RequestMapping({"/", ""})
    public String home() {
        return "/view/event/home";
    }

}
