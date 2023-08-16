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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.mgv.board.list.TheaterBoardList;
import kr.co.mgv.board.service.TheaterBoardService;
import kr.co.mgv.board.vo.BoardTheater;
import kr.co.mgv.board.vo.MBoardLike;
import kr.co.mgv.board.vo.TBoardLike;
import kr.co.mgv.board.vo.TheaterBoard;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board/theater")
@RequiredArgsConstructor
public class TheaterBoardController {
	
	private final TheaterBoardService theaterBoardService;

    @GetMapping("/list")
    public String theaterList(@RequestParam(name = "sort", required = false, defaultValue = "id") String sort,
			@RequestParam(name = "rows", required = false, defaultValue = "10") int rows,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "opt", required = false, defaultValue = "") String opt,
			@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(name = "theaterNo", required = false) Integer theaterNo,
			@RequestParam(name = "locationNo", required = false) Integer locationNo,
			Model model) {
    	
    	Map<String, Object> param = new HashMap<String, Object>();
		param.put("sort", sort);
		param.put("rows", rows);
		param.put("page", page);
		if (theaterNo != null) {
			param.put("theaterNo", theaterNo);
		}
		if (locationNo != null) {
			param.put("locationNo", locationNo);
		}
		if(StringUtils.hasText(opt) && StringUtils.hasText(keyword)) {
			param.put("opt", opt);
			param.put("keyword", keyword);
		}
    	
		// service로 극장게시물 목록, 극장,지역 목록, 페이지네이션 조회하기 
		TheaterBoardList result = theaterBoardService.getTBoards(param);
		
		// model에 조회한 리스트 담기
		model.addAttribute("result", result);
		
        return "/view/board/theater/list";
    }

    
	@GetMapping("/theaterByLocationNo")
	@ResponseBody
	public List<BoardTheater> getTheaterbyLocationNo(@RequestParam("locationNo") int locationNo) {
		List<BoardTheater> theaters = theaterBoardService.getTheatersByLocationNo(locationNo);
		return theaters;
	}
	
	// 상세페이지 관련
	@GetMapping("/read")
	public String read(@RequestParam("no") int no,
					   @RequestParam("page") int page,
					   @RequestParam(name = "rows", required = false, defaultValue = "10") Integer rows,
					   @RequestParam("sort") String sort,
					   @RequestParam(name = "theaterNo", required = false) Integer theaterNo,
					   @RequestParam("opt") String opt,
					   @RequestParam("keyword") String keyword,
					   RedirectAttributes redirectAttributes) {
		
		theaterBoardService.increseRead(no);
		
		redirectAttributes.addAttribute("no", no);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("sort", sort);
        if(rows != null) {
        	redirectAttributes.addAttribute("rows", rows);		
        }
        redirectAttributes.addAttribute("theaterNo", theaterNo);
        redirectAttributes.addAttribute("opt", opt);
        redirectAttributes.addAttribute("keyword", keyword);
		
		return "redirect:/board/theater/detail";
	}
	
	@GetMapping("/detail")
	public String theaterDetail(@RequestParam("no") int no,
								Model model,
								@AuthenticationPrincipal User user) {
		
		if(user != null) {
			TBoardLike like = new TBoardLike();
			like.setUser(user);
			TheaterBoard board = TheaterBoard.builder()
									.no(no)
									.build();
			like.setBoard(board);
			
			TBoardLike savedLike = theaterBoardService.getLike(like);
			model.addAttribute("like", savedLike);
		}
		
		TheaterBoard theaterBoard = theaterBoardService.getTheaterBoardByNo(no);
		model.addAttribute("board", theaterBoard);
		// 모댓글 목록
		// 자손댓글 목록
		// 신고 이유
		
		
		return "/view/board/theater/detail";
	}

	@PostMapping("/changelike")
	@ResponseBody
	public ResponseEntity<Void> addLike(@RequestParam("no") int no,
										@RequestParam("id") String id,
										@RequestParam("likeCount") int likeCount){
		
		TBoardLike like = new TBoardLike();
    	User user = User.builder()
				.id(id)
				.build();
    	like.setUser(user);
    	TheaterBoard board = TheaterBoard.builder()
						.no(no)
						.build();
		like.setBoard(board); 
		TBoardLike savedLike = theaterBoardService.getLike(like);
		
	 	if (savedLike != null && "Y".equals(savedLike.getCancel())) {
    		savedLike.setCancel("N");
    		theaterBoardService.updateTboardLike(savedLike);
    	} else if(savedLike != null && "N".equals(savedLike.getCancel())) {
    		savedLike.setCancel("Y");
    		theaterBoardService.updateTboardLike(savedLike);
    	} else if(savedLike == null) {
    		theaterBoardService.insertBoardLike(like);
    	}
	 	theaterBoardService.updateBoardLike(no, likeCount);
	 	
	 	return ResponseEntity.ok().build();
	}

	// 댓글 관련
	
	// 신고관련 list
}
