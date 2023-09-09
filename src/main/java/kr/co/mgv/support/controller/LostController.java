package kr.co.mgv.support.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.mgv.common.vo.MgvFile;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import kr.co.mgv.support.dao.LostDao;
import kr.co.mgv.support.dto.LostList;
import kr.co.mgv.support.form.AddLostForm;
import kr.co.mgv.support.service.LostService;
import kr.co.mgv.support.vo.Lost;
import kr.co.mgv.support.vo.LostComment;
import kr.co.mgv.support.vo.LostFile;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Controller
@RequestMapping("/support/lost")
@RequiredArgsConstructor
@ToString
public class LostController {
	
	private final LostService lostService;

	@GetMapping
    public String lost(	@RequestParam(name = "locationNo", required = false, defaultValue = "0") int locationNo,
			@RequestParam(name = "theaterNo", required = false, defaultValue = "0") int theaterNo,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "answered", required = false) String answered,
			@RequestParam(name ="keyword", required = false) String keyword,
    		Model model) {
		
		Map<String, Object> param = new HashMap<>();
		param.put("page", page);
		
		if (locationNo != 0) {
			param.put("locationNo", locationNo);
		}
		
		if (theaterNo != 0) {
			param.put("theaterNo", theaterNo);
		} 
		
		if (StringUtils.hasText(answered)) {
			param.put("answered", answered);
		}
	
		if (StringUtils.hasText(keyword)) {
			param.put("keyword", keyword);
		}
		
		LostList lostList = lostService.search(param);
		model.addAttribute("result", lostList);
		
        return "view/support/lost/list";
    }
	
	
	@GetMapping("/list")
	@ResponseBody
	public LostList list(
			@RequestParam(name = "locationNo", required = false, defaultValue = "0") int locationNo,
			@RequestParam(name = "theaterNo", required = false, defaultValue = "0") int theaterNo,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "answered", required = false) String answered,
			@RequestParam(name ="keyword", required = false) String keyword) {
		
		Map<String, Object> param = new HashMap<>();
		param.put("page", page);
		
		if (locationNo != 0) {
			param.put("locationNo", locationNo);
		}
		
		if (theaterNo != 0) {
			param.put("theaterNo", theaterNo);
		} 
		
		if (StringUtils.hasText(answered)) {
			param.put("answered", answered);
		}
	
		if (StringUtils.hasText(keyword)) {
			param.put("keyword", keyword);
		}
		
		LostList lostList = lostService.search(param);
		
		return lostList;
	}
	
	@GetMapping("/mylist")
	@ResponseBody
	public LostList list(@AuthenticationPrincipal User user,
			@RequestParam(name = "locationNo", required = false, defaultValue = "0") int locationNo,
			@RequestParam(name = "theaterNo", required = false, defaultValue = "0") int theaterNo,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "answered", required = false) String answered,
			@RequestParam(name ="keyword", required = false) String keyword,
			@RequestParam(name ="guestName", required = false) String guestName,
			@RequestParam(name ="guestEmail", required = false) String guestEmail){
		
		Map<String, Object> param = new HashMap<>();
		
		param.put("page", page);
		
		if (user != null && StringUtils.hasText(user.getId())) {
			param.put("userId", user.getId());
		}
		
		if (locationNo != 0) {
			param.put("locationNo", locationNo);
		}
		
		if (theaterNo != 0) {
			param.put("theaterNo", theaterNo);
		} 
		
		if (StringUtils.hasText(answered)) {
			param.put("answered", answered);
		}
		
		if (StringUtils.hasText(keyword)) {
			param.put("keyword", keyword);
		}
		
		if (StringUtils.hasText(guestName)) {
			param.put("guestName", guestName);
		}
	
		if (StringUtils.hasText(keyword)) {
			param.put("guestEmail", guestEmail);
		}
		
		LostList lostList = lostService.search(param);
		
		return lostList;
	}

	 
	@GetMapping("/form")
    public String lostForm() {
        return "view/support/lost/form";
    }
	
	@PostMapping("/add")
	public String createLost(@AuthenticationPrincipal User user, AddLostForm form) {
		lostService.insertLost(form, user);
		return "redirect:/support/lost";
	}
	
	@GetMapping("/delete")
	public String deleteLost(@RequestParam("no") int lostNo, Model model) {
		
		lostService.deleteLost(lostNo);
		return "redirect:/support/lost";
	}
	
	
	
	@GetMapping("/detail")
	public String getLostByNo(@RequestParam("no") int lostNo, Model model) {
		Lost lost = lostService.getLostByNo(lostNo);
		List<LostFile> lostFiles = lostService.getLostFilesByLostNo(lostNo);
		List<LostComment> lostComments = lostService.getLostCommentsByLost(lostNo);
		
		model.addAttribute("lost", lost);
		model.addAttribute("lostFiles", lostFiles);
		model.addAttribute("lostComments", lostComments);
		
		return "view/support/lost/detail";
	}

	@GetMapping("/download")
	public ResponseEntity<UrlResource> download(@RequestParam("no") int fileNo) {
		LostFile lostFile = lostService.getLostFileByFileNo(fileNo);

		if (lostFile == null) {
			return ResponseEntity.badRequest().build();
		}

		Path filePath = Paths.get(lostFile.getUploadPath(), File.separator, lostFile.getSaveName());
		UrlResource resource;
		try {
			resource = new UrlResource(filePath.toUri());
		} catch (MalformedURLException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType("application/octet-stream"))
			.header("content-disposition", "attachment;filename=" + lostFile.getOriginalName())
			.body(resource);
	}

	@GetMapping("/getLocation")
	@ResponseBody
	public List<Location> getLocations() {
		return lostService.getLocations();
	}
	
	@GetMapping("/getTheaterByLocationNo")
	@ResponseBody
	public List<Theater> getTheatersByLocationNo(@RequestParam("locationNo") int locationNo) {
		return lostService.getTheatesrByLocationNo(locationNo);
	}
	
	
}















