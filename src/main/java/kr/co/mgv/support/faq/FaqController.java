package kr.co.mgv.support.faq;


import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Controller
@RequestMapping("/support/faq")
@Slf4j
public class FaqController {

	@GetMapping
	public String faq() {
		return "/view/support/faq/list";
	}
	
	@GetMapping("/list")
	@ResponseBody
	public List<Faq> getFaq(@RequestParam("catNo") int catNo) {
		
		return null;
	}
}
