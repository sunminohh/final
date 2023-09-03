package kr.co.mgv.support.controller;


import java.util.HashMap;
import java.util.List;
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
import kr.co.mgv.support.vo.Faq;
import kr.co.mgv.support.vo.Notice;
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
	public String homeList(Model model) {
    	List<Faq> faqs = faqService.getFaqList();
		List<Notice> notices = noticeService.getNoticeList();
    			
		model.addAttribute("faqResult", faqs);
		model.addAttribute("noticeResult", notices);
		
		return "view/support/home";
	}
    
    @GetMapping("/terms")
    public String terms() {
        return "view/support/terms/list";
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "view/support/privacy-policy/list";
    }
}
