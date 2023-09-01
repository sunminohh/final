package kr.co.mgv.board.controller;

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

import kr.co.mgv.board.list.AdminBoardList;
import kr.co.mgv.board.list.BoardList;
import kr.co.mgv.board.list.MyBoardList;
import kr.co.mgv.board.service.AdminBoardService;
import kr.co.mgv.board.vo.BoardReport;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin/board")
@RequiredArgsConstructor
@Slf4j
public class AdminBoardController {

	private final AdminBoardService adminBoardService;
	
	@GetMapping("/reportList")
	public String adminReportList(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
								   @RequestParam(name = "opt", required = false, defaultValue = "") String opt,
						           @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
						           @RequestParam(name = "boards", required = false, defaultValue = "all") String boards,
						           @AuthenticationPrincipal User user,
						           Model model) {
			
		Map<String , Object> param = new HashMap<String, Object>();
		param.put("page", page);
		String id = user.getId();
		param.put("id", id);
		param.put("boards", boards);
		
		log.info("{}",id);
		
		if(StringUtils.hasText(opt) && StringUtils.hasText(keyword)) {
			param.put("opt", opt);
			param.put("keyword", keyword);
		}
		
		MyBoardList result = adminBoardService.getBoardList(param);
		model.addAttribute("result", result);
		
		return "view/admin/board/reportList";
	}
	
	@GetMapping("/boardDetail")
	@ResponseBody
	public ResponseEntity<AdminBoardList> getBoardDetail(@RequestParam("type") String type,
	           											@RequestParam("no") int no){
		
		Map<String , Object> param = new HashMap<String, Object>();
		param.put("type", type);
		param.put("no", no);
		
		List<BoardReport> reports = adminBoardService.getReports(param);
		BoardList board = adminBoardService.getBoardDetail(param);
		AdminBoardList result = AdminBoardList.builder().list(board).reports(reports).build();
		
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("/delete")
	@ResponseBody
	public ResponseEntity<Integer> deleteReportBoard(@RequestParam("type") String type,
													 @RequestParam("no") int no){
		
		// 해당 게시물의 deleted를 Y로 변경
		Map<String , Object> param = new HashMap<String, Object>();
		param.put("type", type);
		param.put("no", no);
		param.put("deleted", "Y");
		adminBoardService.deleteReportBoard(param);
		
		// 변경후 리스트의 전체건수 구하기
		int totalRows = adminBoardService.getTotalrows();
		
		return ResponseEntity.ok().body(totalRows);
	}

	@PostMapping("/restore")
	@ResponseBody
	public ResponseEntity<Integer> deleteRestoreBoard(@RequestParam("type") String type,
			@RequestParam("no") int no){
		
		// 해당 게시물의 신고상태 초기화
		Map<String , Object> param = new HashMap<String, Object>();
		param.put("type", type);
		param.put("no", no);
		param.put("report", "N");
		param.put("reportCount", 0);
		adminBoardService.resotreReportBoard(param);
		
		// 해당 게시물의 신고이유 목록 삭제
		adminBoardService.deleteReportReasonByNo(param);
		
		// 변경후 리스트의 전체건수 구하기
		int totalRows = adminBoardService.getTotalrows();
		
		return ResponseEntity.ok().body(totalRows);
	}
	
}
