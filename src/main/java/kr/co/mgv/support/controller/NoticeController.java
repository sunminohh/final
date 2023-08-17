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

import kr.co.mgv.support.dto.NoticeList;
import kr.co.mgv.support.service.NoticeService;
import kr.co.mgv.support.vo.Notice;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Controller
@RequestMapping("/support/notice")
@RequiredArgsConstructor
@ToString
public class NoticeController {
	
	private final NoticeService noticeService;
	
    @RequestMapping
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
    	
        return "/view/support/notice/list";
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

    @RequestMapping("/detail")
    public String getNoticeByNo(@RequestParam("no") int noticeNo, Model model) {
    	Notice notice = noticeService.getNoticeByNo(noticeNo);
    	
    	Notice preNotice = noticeService.getPrevNotice(noticeNo);
    	Notice nextNotice = noticeService.getNextNotice(noticeNo);
    	
    	model.addAttribute("notice", notice);
    	
    	model.addAttribute("preNotice", preNotice);
    	model.addAttribute("nextNotice", nextNotice);
    	
        return "/view/support/notice/detail";
    }
}



















