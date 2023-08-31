package kr.co.mgv.support.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/support/chat")
public class AdminChatController {

	 @GetMapping
	    public String chat() {
	    	return "view/admin/support/chat";
	 }
	 
}
