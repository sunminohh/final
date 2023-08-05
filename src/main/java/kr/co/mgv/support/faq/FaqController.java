package kr.co.mgv.support.faq;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/support/faq")
@RequiredArgsConstructor
public class FaqController {

	@GetMapping
	public String faq() {
		return "/view/support/faq/list";
	}
	
	@GetMapping("/list")
	public List<Map<String, String>> getFaq() {
		
		return null;
	}
}
