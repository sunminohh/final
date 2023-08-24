package kr.co.mgv.support.controller;



import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.mgv.support.dto.FaqList;
import kr.co.mgv.support.form.AddFaqForm;
import kr.co.mgv.support.form.ModifyFaqForm;
import kr.co.mgv.support.service.FaqService;
import kr.co.mgv.support.vo.Faq;
import kr.co.mgv.support.vo.SupportCategory;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/support/faq")
@RequiredArgsConstructor
public class AdminFaqController {
	
	private final FaqService faqService;
	
	@RequestMapping
	public String faq(@RequestParam(name = "catNo", required = false, defaultValue = "1") int catNo,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "keyword", required = false) String keyword,
			Model model) {
		
		Map<String, Object> param = new HashMap<>();
		param.put("catNo", catNo);
		param.put("page", page);
		FaqList faqList = faqService.search(param);
		
		model.addAttribute("result", faqList);
		
		return "/view/admin/support/faq/list";
	}
	
	@GetMapping("/list")
	@ResponseBody
	public FaqList getFaq(@RequestParam(name = "catNo", required = false, defaultValue = "1") int catNo,
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
	
	@RequestMapping("/detail") 
	public String getFaqByNo(@RequestParam("no") int faqNo, Model model) {
		Faq faq = faqService.getFaqByNo(faqNo);
		model.addAttribute("faq", faq);
		
		return "/view/admin/support/faq/detail";
	}
	
	@GetMapping("/form")
	public String faqForm() {
		return "/view/admin/support/faq/form";
	}
	
	@GetMapping("/modifyform")
	public String faqmodifyForm(@RequestParam("no") int faqNo, Model model) {
		model.addAttribute("faq", faqService.getFaqByNo(faqNo));
		
		model.addAttribute("categories", faqService.getCategoriesByType("faq"));
		
		return "/view/admin/support/faq/modifyform";
	}
	
	@PostMapping("/add")
	public String insertFaq(@AuthenticationPrincipal User user, AddFaqForm form) {
		faqService.insertFaq(form, user);
		return "redirect:/admin/support/faq";
	}
	
	@PostMapping("/modify")
	public String modifyFaq(@RequestParam("no") int faqNo, ModifyFaqForm form) {
		faqService.modifyFaq(form, faqNo);
		return "redirect:/admin/support/faq/detail?no=" + faqNo;
	}
	
	@GetMapping("/delete")
	public String deleteFaq(@RequestParam("no") int faqNo, Model model) {
		faqService.deleteFaq(faqNo);
		return "redirect:/admin/support/faq";
	}
	
	@GetMapping("/getCategory")
	@ResponseBody
	public List<SupportCategory> getCategories(@RequestParam String type) {
		return faqService.getCategoriesByType(type);
	}
	
	
}












