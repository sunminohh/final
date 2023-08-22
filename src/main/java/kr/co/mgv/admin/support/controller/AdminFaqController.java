package kr.co.mgv.admin.support.controller;



import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.mgv.support.dto.FaqList;
import kr.co.mgv.support.service.FaqService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/support/faq")
@RequiredArgsConstructor
public class AdminFaqController {
	
	private final FaqService faqService;
	
	@GetMapping
	public String faq(Model model) {
		Map<String, Object> param = new HashMap<>();
		param.put("catNo", 1);
		param.put("page", 1);
		FaqList faqList = faqService.search(param);
		
		model.addAttribute("result", faqList);
		
		return "/view/admin/support/faq/list";
	}
	
	@GetMapping("/list")
	@ResponseBody
	public FaqList getFaq(@RequestParam(name = "catNo") int catNo,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "keyword", required = false) String keyword) {
		
		Map<String, Object> param = new HashMap<>();
		param.put("catNo", catNo);
		param.put("page", page);
		if (StringUtils.hasText(keyword)) {
			param.put("keyword", keyword);
		}
		
		FaqList faqList = faqService.search(param);
		
		return faqList;
	}
	
	
}
