package kr.co.mgv.support.controller;


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
import kr.co.mgv.support.dto.NoticeList;
import kr.co.mgv.support.service.FaqService;
import kr.co.mgv.support.service.NoticeService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/support")
@RequiredArgsConstructor
public class HomeCotroller {
	
	private final FaqService faqService;
	private final NoticeService noticeService;
	
    @RequestMapping({"/", ""})
    public String home() {
        return "view/support/home";
    }
    
    @GetMapping
	public String faq(Model model) {
		Map<String, Object> param = new HashMap<>();
		param.put("catNo", 1);
		param.put("page", 1);
		FaqList faqList = faqService.search(param);
		
		model.addAttribute("faqResult", faqList);
		
		
		NoticeList noticeList = noticeService.search(param);
		model.addAttribute("noticeList", noticeList);
		
		
		return "view/support/home";
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
	 /*
	 @GetMapping
	    public String notice(@RequestParam(name = "catNo", required = false, defaultValue = "21") int catNo,
	    		@RequestParam(name = "page", required = false, defaultValue = "1") int page,
	    		@RequestParam(name = "locationNo", required = false, defaultValue = "0") int locationNo,
				@RequestParam(name = "theaterNo", required = false, defaultValue = "0") int theaterNo,
				@RequestParam(name ="keyword", required = false) String keyword, 
				Model model) {
	    	
	    	Map<String, Object> param = new HashMap<>();
	    	param.put("catNo", catNo);
	    	param.put("page", page);
	    	
	    	if (locationNo != 0) {
				param.put("locationNo", locationNo);
			}
			
			if (theaterNo != 0) {
				param.put("theaterNo", theaterNo);
			}
			
	    	if (StringUtils.hasText(keyword)) {
	    		param.put("keyword", keyword);
	    	}
	    	NoticeList noticeList = noticeService.search(param);
	    	model.addAttribute("result", noticeList);
	    	
	        return "view/support/home";
	    }
	    
	 	@GetMapping("/list")
	    @ResponseBody
	    public NoticeList getNotice(@RequestParam(name = "catNo", required = false, defaultValue = "21") int catNo,
	    		@RequestParam(name = "page", required = false, defaultValue = "1") int page,
	    		@RequestParam(name = "locationNo", required = false, defaultValue = "0") int locationNo,
				@RequestParam(name = "theaterNo", required = false, defaultValue = "0") int theaterNo,
				@RequestParam(name ="keyword", required = false) String keyword) {
	    	
	    	Map<String, Object> param = new HashMap<>();
	    	param.put("catNo", catNo);
	    	param.put("page", page);
	    	
	    	if (locationNo != 0) {
				param.put("locationNo", locationNo);
			}
			
			if (theaterNo != 0) {
				param.put("theaterNo", theaterNo);
			}
			
	    	if (StringUtils.hasText(keyword)) {
	    		param.put("keyword", keyword);
	    	}
	    	
	    	NoticeList noticeList = noticeService.search(param);
	    	
	    	return noticeList;
	    }
	    
	    */
    
    @GetMapping("/terms")
    public String terms() {
        return "view/support/terms/list";
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "view/support/privacy-policy/list";
    }
}
