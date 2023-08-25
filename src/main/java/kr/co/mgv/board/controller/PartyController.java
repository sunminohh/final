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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.mgv.board.form.AddPboardForm;
import kr.co.mgv.board.list.PartyBoardList;
import kr.co.mgv.board.service.MovieBoardService;
import kr.co.mgv.board.service.PartyBoardService;
import kr.co.mgv.board.service.TheaterBoardService;
import kr.co.mgv.board.vo.BoardLocation;
import kr.co.mgv.board.vo.BoardTheater;
import kr.co.mgv.board.vo.PartyBoard;
import kr.co.mgv.board.vo.PartyBoardSchedule;
import kr.co.mgv.board.vo.PartyJoin;
import kr.co.mgv.board.vo.ReportReason;
import kr.co.mgv.board.vo.TBoardComment;
import kr.co.mgv.board.vo.TBoardLike;
import kr.co.mgv.board.vo.TheaterBoard;
import kr.co.mgv.movie.vo.Movie;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/board/party")
@RequiredArgsConstructor
@Slf4j
public class PartyController {

		private final PartyBoardService partyBoardService;
		private final TheaterBoardService theaterBoardService;
		private final MovieBoardService movieBoardService;

		@GetMapping("/list")
	    public String theaterList(@RequestParam(name = "sort", required = false, defaultValue = "id") String sort,
				@RequestParam(name = "rows", required = false, defaultValue = "10") int rows,
				@RequestParam(name = "page", required = false, defaultValue = "1") int page,
				@RequestParam(name = "opt", required = false, defaultValue = "") String opt,
				@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
				@RequestParam(name = "theaterNo", required = false) Integer theaterNo,
				@RequestParam(name = "locationNo", required = false) Integer locationNo,
				@RequestParam(name = "complete", required = false, defaultValue = "E") String complete,
				Model model) {
	    	
	    	Map<String, Object> param = new HashMap<String, Object>();
			param.put("sort", sort);
			param.put("rows", rows);
			param.put("page", page);
			param.put("complete", complete);
			
			if (theaterNo != null) {
				param.put("theaterNo", theaterNo);
			}
			if (locationNo != null) {
				param.put("locationNo", locationNo);
				List<BoardTheater> theaters = theaterBoardService.getTheatersByLocationNo(locationNo);
				model.addAttribute("theaters", theaters);
			}

			if(StringUtils.hasText(opt) && StringUtils.hasText(keyword)) {
				param.put("opt", opt);
				param.put("keyword", keyword);
			}
	    	
			// service로 극장게시물 목록, 극장, 지역목록, 페이지네이션 조회하기 
			PartyBoardList result = partyBoardService.getPBoards(param);
			
			// 신고
			
			// model에 조회한 극장게시물 담기
			model.addAttribute("result", result);
			
			log.info(complete);
			log.info(sort);
			
	        return "/view/board/party/list";
		}

	    // 게시물 등록 폼 관련
		@GetMapping("/theaterByLocationNo")
		@ResponseBody
		public List<BoardTheater> getTheaterbyLocationNo(@RequestParam("locationNo") int locationNo) {
			List<BoardTheater> theaters = theaterBoardService.getTheatersByLocationNo(locationNo);
			return theaters;
		}
	    
	    @GetMapping("/add")
	    public String theaterForm(Model model) {
	    	List<BoardLocation> locations = theaterBoardService.getLocations();
	    	model.addAttribute("locations", locations);
	    	
	    	List<Movie> movies = movieBoardService.getMovieTitle();
	    	model.addAttribute("movies", movies);
	    	
	        return "/view/board/party/form";
	    }
	    
	    
	    @GetMapping("/scheduleBydateAndMNoAndTno")
	    @ResponseBody
	    public ResponseEntity<List<PartyBoardSchedule>> getSchedules(
	            @RequestParam("theaterNo") Integer theaterNo,
	            @RequestParam("movieNo") Integer movieNo,
	            @RequestParam("date") String date) {

	        if (theaterNo == null || movieNo == null || date == null) {
	            return ResponseEntity.badRequest().build();
	        }
	        
	        Map<String, Object> param = new HashMap<String, Object>();
	        param.put("theaterNo", theaterNo);
	        param.put("movieNo", movieNo);
	        param.put("date", date);
	        List<PartyBoardSchedule> schedules = partyBoardService.getScheduleList(param);
	        
	        return ResponseEntity.ok().body(schedules);
	    }
	
	    @GetMapping("/selectedInfoBySId")
	    @ResponseBody
	    public ResponseEntity<PartyBoardSchedule> getSchedules(
	    		@RequestParam("scheduleId") int scheduleId) {
	    	
	    	PartyBoardSchedule schedule = partyBoardService.getSceduleById(scheduleId);
	    	
	    	return ResponseEntity.ok().body(schedule);
	    }
	    
	    // 게시물 등록 관련
	    @PostMapping("/add")
	    public String addPartyBoard(AddPboardForm form, @AuthenticationPrincipal User user) {
	    	
	    	partyBoardService.insertPBoard(form, user);
	    	
	    	return "redirect:/board/party/list";
	    }
	    
	    // 상세화면 관련
		@GetMapping("/read")
		public String read(@RequestParam("no") int no,
						   @RequestParam("page") int page,
						   @RequestParam(name = "rows", required = false, defaultValue = "10") Integer rows,
						   @RequestParam("sort") String sort,
						   @RequestParam("complete") String complete,
						   @RequestParam(name = "theaterNo", required = false) Integer theaterNo,
						   @RequestParam("opt") String opt,
						   @RequestParam("keyword") String keyword,
						   RedirectAttributes redirectAttributes) {
			
			// 조회수 증가
			partyBoardService.increaseReadCount(no);
			
			redirectAttributes.addAttribute("no", no);
	        redirectAttributes.addAttribute("page", page);
	        redirectAttributes.addAttribute("sort", sort);
	        redirectAttributes.addAttribute("complete", complete);
	        if(rows != null) {
	        	redirectAttributes.addAttribute("rows", rows);		
	        }
	        redirectAttributes.addAttribute("theaterNo", theaterNo);
	        redirectAttributes.addAttribute("opt", opt);
	        redirectAttributes.addAttribute("keyword", keyword);
			
			return "redirect:/board/party/detail";
		}
	    
		
		@GetMapping("/detail")
		public String theaterDetail(@RequestParam("no") int no,
									Model model,
									@AuthenticationPrincipal User user) {
			// 신청버튼
			if(user != null) {
				PartyJoin savedjoin = partyBoardService.getJoinByPnoAndId(no, user);
				model.addAttribute("join", savedjoin);
			}
			
			// 게시물 번호로 게시물 조회
			PartyBoard partyBoard = partyBoardService.getPBoardByNo(no);
			model.addAttribute("board", partyBoard);
			// 모댓글 목록

			// 자손댓글 목록

			// 신고 이유
			List<ReportReason> reportReasons = movieBoardService.getReportReason();
			model.addAttribute("reasons", reportReasons);
			
			// 수락된 신청자 목록
			List<PartyJoin> acceptedJoins = partyBoardService.getJoinByPnoAndAccept(no, "Y");
			model.addAttribute("accepts", acceptedJoins);
			
			// 수락 안된 신청자 목록
			List<PartyJoin> notAcceptedJoins = partyBoardService.getJoinByPnoAndAccept(no, "N");
			model.addAttribute("notAccepts", notAcceptedJoins);
			
			log.info(partyBoard.getContent());
			return "/view/board/party/detail";
		}
		
		// 신청 버튼 관련
		@PostMapping("/changeRequest")
		@ResponseBody
		public ResponseEntity<Void> changeRequest(@RequestParam("no") int no,
												  @RequestParam("request") String request,
												  @AuthenticationPrincipal User user){
			PartyJoin savedJoin = partyBoardService.getJoinByPnoAndId(no, user);
			PartyBoard savedBoard = partyBoardService.getPBoardByNo(no);
			int requestCount = savedBoard.getRequestCount();
			
			if(savedJoin != null && "Y".equals(savedJoin.getRequest())) {
				partyBoardService.updateJoin(no, user, request);
				savedBoard.setRequestCount(requestCount - 1);
			} else if (savedJoin != null && "N".equals(savedJoin.getRequest())) {
				partyBoardService.updateJoin(no, user, request);
				savedBoard.setRequestCount(requestCount + 1);
			} else if (savedJoin == null) {
				partyBoardService.insertPartyJoin(no, user);
				savedBoard.setRequestCount(requestCount + 1);
			}
			partyBoardService.updateRequestCount(savedBoard);
			
			return ResponseEntity.ok().build();
		}

	    
}
