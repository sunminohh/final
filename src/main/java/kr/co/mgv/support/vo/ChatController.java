package kr.co.mgv.support.vo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/support/chat")
@RequiredArgsConstructor
public class ChatController {

	@GetMapping
	public String chat() {
		return "view/support/chat";
	}
	
}
