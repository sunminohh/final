package kr.co.mgv.support.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/support/guest")
@RequiredArgsConstructor
public class GuestController {

	@GetMapping("/form")
    public String guestForm() {
        return "view/support/guest/form";
    }
	
}
