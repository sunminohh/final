package kr.co.mgv.support.controller;


import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.co.mgv.support.dto.OneList;
import kr.co.mgv.support.form.AddOneForm;
import kr.co.mgv.support.service.LostService;
import kr.co.mgv.support.service.OneService;
import kr.co.mgv.support.vo.Lost;
import kr.co.mgv.support.vo.LostComment;
import kr.co.mgv.support.vo.LostFile;
import kr.co.mgv.support.vo.One;
import kr.co.mgv.support.vo.OneComment;
import kr.co.mgv.support.vo.OneFile;
import kr.co.mgv.support.vo.SupportCategory;
import kr.co.mgv.theater.vo.Location;
import kr.co.mgv.theater.vo.Theater;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/support/one")
@RequiredArgsConstructor
public class OneController {

	private final OneService oneService;
	private final LostService lostService;
	
	@GetMapping()
    public String one() {
        return "view/support/one/form";
    }
	
	@GetMapping("/myinquery")
	public String myinquery() {

		return "view/support/one/list";
	}
	
	@GetMapping("/list")
	@ResponseBody
	public OneList list(@AuthenticationPrincipal User user,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "answered", required = false) String answered,
			@RequestParam(name ="keyword", required = false) String keyword,
			@RequestParam(name ="guestName", required = false) String guestName,
			@RequestParam(name ="guestEmail", required = false) String guestEmail,
			Model model) {
		
		Map<String, Object> param = new HashMap<>();
		
		param.put("page", page);
		
		if (user != null && StringUtils.hasText(user.getId())) {
			param.put("userId", user.getId());
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
		
		OneList oneList = oneService.search(param);
		
		return oneList;
	}
	
	
	
	@PostMapping("/add")
	public String insertOne(@AuthenticationPrincipal User user, AddOneForm form) {
		oneService.insertOne(form, user);
		return "redirect:/support/one";
	}
	
	@GetMapping("/getCategory")
	@ResponseBody
	public List<SupportCategory> getCategories(@RequestParam String type) {
		
		return oneService.getCategoriesByType(type);
	}
	
	@GetMapping("/myinquery/detail")
	public String getOneByNo(@RequestParam("no") int oneNo, Model model) {
		One one = oneService.getOneByNo(oneNo);
		List<OneFile> oneFiles = oneService.getOneFileByOneNo(oneNo);
		List<OneComment> oneComments = oneService.getOneCommentByOne(oneNo);
		
		model.addAttribute("one", one);
		model.addAttribute("oneFiles", oneFiles);
		model.addAttribute("oneComments", oneComments);
		
		return "view/support/one/detail";
	}
	
	@RequestMapping("/myinquery/download")
	public ResponseEntity<UrlResource> download(@RequestParam("no") int fileNo) {
		OneFile oneFile = oneService.getOneFileByFileNo(fileNo);

		if (oneFile == null) {
			return ResponseEntity.badRequest().build();
		}

		Path filePath = Paths.get(oneFile.getUploadPath(), File.separator, oneFile.getSaveName());
		UrlResource resource;
		try {
			resource = new UrlResource(filePath.toUri());
		} catch (MalformedURLException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType("application/octet-stream"))
			.header("content-disposition", "attachment;filename=" + oneFile.getOriginalName())
			.body(resource);
	}
	
	@GetMapping("/mylost/detail")
	public String getMyLostByNo(@RequestParam("no") int lostNo, Model model) {
		Lost lost = lostService.getLostByNo(lostNo);
		List<LostFile> lostFiles = lostService.getLostFilesByLostNo(lostNo);
		List<LostComment> lostComments = lostService.getLostCommentsByLost(lostNo);
		
		model.addAttribute("lost", lost);
		model.addAttribute("lostFiles", lostFiles);
		model.addAttribute("lostComments", lostComments);
		
		return "view/support/one/lostdetail";
	}
	
	@GetMapping("/delete")
	public String deleteOne(@RequestParam("no") int oneNo, Model model) {
		
		oneService.deleteOne(oneNo);
		return "redirect:/support/one/myinquery";
	}
	
	@GetMapping("/mylost/delete")
	public String deletemyLost(@RequestParam("no") int lostNo, Model model) {
		
		lostService.deleteLost(lostNo);
		return "redirect:/support/one/myinquery?tab=tab-lost";
	}
	
	
	
	@GetMapping("/getLocation")
	@ResponseBody
	public List<Location> getLocations() {
		
		return oneService.getLocations();
	}
	
	@GetMapping("/getTheaterByLocationNo")
	@ResponseBody
	public List<Theater> getTheatersByLocationNo(@RequestParam("locationNo") int locationNo) {
		
		return oneService.getTheatesrByLocationNo(locationNo);
	}
	
}
