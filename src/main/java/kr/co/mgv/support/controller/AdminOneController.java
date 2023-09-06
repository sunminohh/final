package kr.co.mgv.support.controller;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
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
import kr.co.mgv.user.service.EmailService;
import kr.co.mgv.user.service.EmailServiceImpl;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/support/one")
@Slf4j
public class AdminOneController {

	private final OneService oneService;
	private final EmailServiceImpl emailService;
	
	@GetMapping 
	public String one(@RequestParam(name = "categoryNo", required = false, defaultValue = "24") int categoryNo,
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
	
	@GetMapping("/delete")
	public String delete(@RequestParam("no") int oneNO, Model model) {
		oneService.deleteOne(oneNO);
		return "redirect:/admin/support/one";
	}
	
	@PostMapping("/addComment")
	@ResponseBody
	public ResponseEntity<List<OneComment>> addComment(@AuthenticationPrincipal User user,
			@RequestParam("no") int oneNo,
			@RequestParam("content") String content) throws Exception {
		
		oneService.insertComment(user, oneNo, content);
		List<OneComment> inputComments = oneService.getOneCommentByOne(oneNo);
		
		return ResponseEntity.ok().body(inputComments);
	}
	
	@PostMapping("/mail")
    @ResponseBody
    public ResponseEntity<String> mailConfirm(@RequestParam("email") String email, HttpSession session) {
        try {
            String content = emailService.sendTempqnaMessage(email);
            log.info("메일내용 -> {}", content);

            // 생성한 인증 코드를 세션에 저장
            return ResponseEntity.ok().body("success");
        } catch (Exception e) {
            log.error("Error sending email", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메일 전송 중 오류가 발생했습니다.");
        }
    }
	
	@PostMapping("/deleteComment")
	@ResponseBody
	public ResponseEntity<Integer> deleteComment(@AuthenticationPrincipal User user,
			@RequestParam("commentNo") int commentNo) {
		
		oneService.deleteComment(commentNo);
		
		return ResponseEntity.ok(commentNo);
	}
	
	
	
}
