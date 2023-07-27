package kr.co.mgv.booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @RequestMapping({"/", ""})
    public String home() {
        return "/view/booking/home";
    }

}
