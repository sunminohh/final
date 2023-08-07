package kr.co.mgv.support.faq;


import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Controller
@RequestMapping("/support/faq")
@RequiredArgsConstructor
public class FaqController {

	private final FaqService faqService;
	
	@GetMapping
	public String faq(Model model) {
		List<Faq> faqList = faqService.getFaqList(1);
		model.addAttribute("faqList", faqList);
		
		return "/view/support/faq/list";
	}
	
	@GetMapping("/list")
	@ResponseBody
	public List<Faq> getFaq(@RequestParam("catNo") int catNo) {
		List<Faq> faqList = faqService.getFaqList(catNo);
		
		return faqList;
	}
}
