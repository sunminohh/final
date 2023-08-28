package kr.co.mgv.support.controller;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

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

import kr.co.mgv.support.dto.OneList;
import kr.co.mgv.support.service.OneService;
import kr.co.mgv.support.vo.One;
import kr.co.mgv.support.vo.OneComment;
import kr.co.mgv.support.vo.OneFile;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/support/one")
public class AdminOneController {

	private final OneService oneService;
	
	@GetMapping String one(@RequestParam(name = "categoryNo", required = false, defaultValue = "24") int categoryNo,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "answered", required = false) String answered,
			@RequestParam(name ="keyword", required = false) String keyword,
			Model model) {
		
		Map<String, Object> param = new HashMap<>();
		param.put("categoryNo", categoryNo);
		param.put("page", page);
		
		if (StringUtils.hasText(answered)) {
			param.put("answered", answered);
		}
	
		if (StringUtils.hasText(keyword)) {
			param.put("keyword", keyword);
		}
		
		OneList oneList = oneService.search(param);	
		model.addAttribute("result", oneList);
		
		return "view/admin/support/one/list";
	}
	
	@GetMapping("/list")
	@ResponseBody
	public OneList list(@RequestParam(name = "categoryNo", required = false, defaultValue = "24") int categoryNo,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "answered", required = false) String answered,
			@RequestParam(name ="keyword", required = false) String keyword) {
		
		Map<String, Object> param = new HashMap<>();
		param.put("categoryNo", categoryNo);
		param.put("page", page);
		
		if (StringUtils.hasText(answered)) {
			param.put("answered", answered);
		}
	
		if (StringUtils.hasText(keyword)) {
			param.put("keyword", keyword);
		}
		
		OneList oneList = oneService.search(param);
		
		return oneList;
	}
	
	@GetMapping("/detail")
	public String getOneByNo(@RequestParam("no") int oneNo, Model model) {
		One one = oneService.getOneByNo(oneNo);
		List<OneFile> oneFiles = oneService.getOneFileByOneNo(oneNo);
		List<OneComment> oneComment = oneService.getOneCommentByOne(oneNo);
		
		model.addAttribute("one", one);
		model.addAttribute("oneFiles", oneFiles);
		model.addAttribute("oneComment", oneComment);
		
		return "view/admin/support/one/detail";
	}
	
	@PostMapping("/addComment")
	@ResponseBody
	public ResponseEntity<List<OneComment>> addComment(@AuthenticationPrincipal User user,
			@RequestParam("no") int oneNo,
			@RequestParam("content") String content) {
		
		One one = One.builder().no(oneNo).build();
		OneComment comment = OneComment.builder().
							user(user).
							one(one).
							content(content).build();
		
		oneService.insertComment(comment);
		oneService.updateOneComment(oneNo);
		
		List<OneComment> inputComments = oneService.getOneCommentByOne(oneNo);
		
		return ResponseEntity.ok().body(inputComments);
	}
	
	
	
	
	
	
	@GetMapping("/delete")
	public String delete(@RequestParam("no") int oneNO, Model model) {
		oneService.deleteOne(oneNO);
		return "redirect:/admin/support/one";
	}
	
	
}
