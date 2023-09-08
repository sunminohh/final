package kr.co.mgv.board.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
import kr.co.mgv.board.form.ReportForm;
import kr.co.mgv.board.list.JoinList;
import kr.co.mgv.board.list.PBoardCommentList;
import kr.co.mgv.board.list.PartyBoardList;
import kr.co.mgv.board.service.MovieBoardService;
import kr.co.mgv.board.service.PartyBoardService;
import kr.co.mgv.board.service.TheaterBoardService;
import kr.co.mgv.board.vo.BoardLocation;
import kr.co.mgv.board.vo.BoardTheater;
import kr.co.mgv.board.vo.PBoardComment;
import kr.co.mgv.board.vo.PBoardReport;
import kr.co.mgv.board.vo.PartyBoard;
import kr.co.mgv.board.vo.PartyBoardSchedule;
import kr.co.mgv.board.vo.PartyJoin;
import kr.co.mgv.board.vo.ReportReason;
import kr.co.mgv.board.vo.SBoardComment;
import kr.co.mgv.board.vo.StoreBoard;
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
				@AuthenticationPrincipal User user,
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
			if (user != null) {
				List<PBoardReport> reportList = partyBoardService.getPBoardReportById(user.getId());
				List<PartyBoard> partyBoardsToShow = new ArrayList<>();
				
				for(PartyBoard board : result.getParytBoards()) {
					boolean isReported = reportList.stream()
										.anyMatch(report -> report.getBoard().getNo() == board.getNo());
					if(!isReported) {
						partyBoardsToShow.add(board);
					}
				}
				result.setParytBoards(partyBoardsToShow);
				model.addAttribute("reports", partyBoardsToShow);
			}
			
			// model에 조회한 극장게시물 담기
			model.addAttribute("result", result);
			
			log.info(complete);
			log.info(sort);
			
	        return "view/board/party/list";
		}

	    // 게시물 등록 폼 관련
		@GetMapping("/theaterByLocationNo")
		@ResponseBody
		public List<BoardTheater> getTheaterbyLocationNo(@RequestParam("locationNo") int locationNo) {
			List<BoardTheater> theaters = theaterBoardService.getTheatersByLocationNo(locationNo);
			return theaters;
		}
	    
       @GetMapping("/add")
       public String theaterForm(Model model, @RequestParam(value="movieTitle", required = false) String inputMovieTitle) {
          List<BoardLocation> locations = theaterBoardService.getLocations();
          model.addAttribute("locations", locations);
          model.addAttribute("inputMovieTitle", inputMovieTitle);
          List<Movie> movies = movieBoardService.getMovieTitle();
          model.addAttribute("movies", movies);
          
           return "view/board/party/form";
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
						   @RequestParam(name = "locationNo", required = false) Integer locationNo,
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
	        redirectAttributes.addAttribute("locationNo", locationNo);
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
			List<PBoardComment> comments = partyBoardService.getGreatComments(no);
			model.addAttribute("comments", comments);
			// 자손댓글 목록
			List<PBoardComment> childComments = partyBoardService.getchildComments(no);
			model.addAttribute("childComments", childComments);
			
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
			return "view/board/party/detail";
		}
		
		// 신청 버튼 관련
		@PostMapping("/changeRequest")
		@ResponseBody
		public ResponseEntity<Void> changeRequest(@RequestParam("no") int no,
												  @RequestParam("request") String request,
												  @AuthenticationPrincipal User user,
												  @RequestParam("writerId") String writerId) throws IOException{
			PartyJoin savedJoin = partyBoardService.getJoinByPnoAndId(no, user);
			PartyBoard savedBoard = partyBoardService.getPBoardByNo(no);
			int requestCount = savedBoard.getRequestCount();
			
			if(savedJoin != null && "Y".equals(savedJoin.getRequest())) {
				partyBoardService.updateJoin(no, user, request, writerId);
				savedBoard.setRequestCount(requestCount - 1);
			} else if (savedJoin != null && "N".equals(savedJoin.getRequest())) {
				partyBoardService.updateJoin(no, user, request, writerId);
				savedBoard.setRequestCount(requestCount + 1);
			} else if (savedJoin == null) {
				partyBoardService.insertPartyJoin(no, user);
				savedBoard.setRequestCount(requestCount + 1);
			}
			partyBoardService.updateRequestCount(savedBoard);
			
			return ResponseEntity.ok().build();
		}
		
		@PostMapping("/join")
		@ResponseBody
		public ResponseEntity<JoinList> acceptJoin(@RequestParam("boardNo") int no,
				  								   @RequestParam("partyId") String id){
			// 신청자의 accept를 Y로 바꾼다.
			String accept = "Y";
			partyBoardService.acceptJoin(no, id, accept);
			
			// 해당 게시글의 acceptCount를 1증가시킨다.
			PartyBoard savedboard = partyBoardService.getPBoardByNo(no);
			int acceptCount = savedboard.getAcceptCount() + 1;
			partyBoardService.updateAcceptCount(no, acceptCount);
			
			// 업데이트된 신청자목록을 조회
			// 수락된 신청자 목록
			List<PartyJoin> acceptedJoins = partyBoardService.getJoinByPnoAndAccept(no, "Y");
			// 수락 안된 신청자 목록
			List<PartyJoin> notAcceptedJoins = partyBoardService.getJoinByPnoAndAccept(no, "N");
			
			int newAcceptCount = partyBoardService.getAcceptCount(no) ;
			
			JoinList list = JoinList.builder().acceptedJoins(acceptedJoins).notAcceptedJoins(notAcceptedJoins).acceptCount(newAcceptCount).build();
			
			return ResponseEntity.ok().body(list);
		}
	
		@PostMapping("/resetJoin")
		@ResponseBody
		public ResponseEntity<JoinList> resetJoin(@RequestParam("boardNo") int no,
												  @RequestParam("partyId") String id){
			// 신청자의 accept를 N로 바꾼다.
			String accept = "N";
			partyBoardService.acceptJoin(no, id, accept);
			
			// 해당 게시글의 acceptCount를 1 감소시킨다.
			PartyBoard savedboard = partyBoardService.getPBoardByNo(no);
			int acceptCount = savedboard.getAcceptCount() - 1;
			partyBoardService.updateAcceptCount(no, acceptCount);
			
			// 업데이트된 신청자목록을 조회
			// 수락된 신청자 목록
			List<PartyJoin> acceptedJoins = partyBoardService.getJoinByPnoAndAccept(no, "Y");
			// 수락 안된 신청자 목록
			List<PartyJoin> notAcceptedJoins = partyBoardService.getJoinByPnoAndAccept(no, "N");
			
			int newAcceptCount = partyBoardService.getAcceptCount(no);
			
			JoinList list = JoinList.builder().acceptedJoins(acceptedJoins).notAcceptedJoins(notAcceptedJoins).acceptCount(newAcceptCount).build();
			return ResponseEntity.ok().body(list);
		}

		@PostMapping("/partyComplete")
		@ResponseBody
		public ResponseEntity<Void> partyComplete(@RequestParam("no") int no) throws IOException{
			
			partyBoardService.partyComplete(no);
			
			return ResponseEntity.ok().build();
		}
		
		// 게시물 수정
		@GetMapping("/modify")
	    public String modifyForm (@RequestParam("no") int no, Model model) {
			List<BoardLocation> locations = theaterBoardService.getLocations();
	    	model.addAttribute("locations", locations);
	    	
	    	List<Movie> movies = movieBoardService.getMovieTitle();
	    	model.addAttribute("movies", movies);
			
	    	PartyBoard board = partyBoardService.getPBoardByNo(no);
	    	model.addAttribute("board", board);
	    	
			return  "/view/board/party/modifyForm";
		}
		
		@PostMapping("/modify")
		public String modifyPBoard (@RequestParam("no") int no, AddPboardForm form) {
			
			partyBoardService.updatePBoard(no, form);
			
			return "redirect:/board/party/detail?no=" + no;
		}
		
		// 게시물 삭제
		@GetMapping("/delete")
		public String deletePBoard (@RequestParam("no") int no) {
			
			String deleted = "Y";
			partyBoardService.deletePBoard(no, deleted);
			
			return "redirect:/board/party/list";
		}

		// 신고 관련
		@PostMapping("/report")
		public String reportBoard(ReportForm form, @AuthenticationPrincipal User user) {
			partyBoardService.insertReport(form, user);
			
			PartyBoard board = partyBoardService.getPBoardByNo(form.getBoardNo());
			int reportCount = board.getReportCount()+1;
			partyBoardService.updateReportCount(form.getBoardNo(), reportCount);
			
			if (reportCount == 5) {
				String report = "Y";
			    partyBoardService.updateReport(form.getBoardNo(), report);
			}
			return "redirect:/board/party/list";
		}
		
		// 댓글관련
		@PostMapping("/addComment")
		@ResponseBody
		public ResponseEntity<PBoardCommentList> addComment(@RequestParam("no") int no, 
												            @RequestParam("id") String id, 
												            @RequestParam(name="parentNo", required = false) Integer parentNo, 
												            @RequestParam(name="greatNo", required = false) Integer greatNo, 
												            @RequestParam("content") String content,
												            @RequestParam("writerId") String writerId) throws IOException {
			
			PBoardComment comment = new PBoardComment();
			comment.setContent(content);
			
			PartyBoard pBoard = partyBoardService.getPBoardByNo(no);
			comment.setBoard(pBoard);
			
			if (parentNo != null) {
				PBoardComment parentComment = PBoardComment.builder().no(parentNo).build();
				comment.setParent(parentComment);
				}
			if (greatNo != null) {
			PBoardComment greatComment = PBoardComment.builder().no(greatNo).build();
			comment.setGreat(greatComment);
			}
			User user = User.builder().id(id).build();
			comment.setUser(user);
			
			partyBoardService.insertComment(comment, writerId);
			PartyBoard board = partyBoardService.getPBoardByNo(no);
			int commentCount = board.getCommentCount() + 1;
			partyBoardService.updateBoardComment(no, commentCount);
			
			List<PBoardComment> parents = partyBoardService.getGreatComments(no);
			List<PBoardComment> childs = partyBoardService.getchildComments(no);
			
			PBoardCommentList list = PBoardCommentList
									 .builder()
									 .parentComments(parents)
									 .childComments(childs)
									 .build();
			
			return ResponseEntity.ok().body(list);
		}

		@PostMapping("/addReComment")
		@ResponseBody
		public ResponseEntity<PBoardCommentList> addReComment(@RequestParam("no") int no, 
				@RequestParam("id") String id, 
				@RequestParam(name="parentNo", required = false) Integer parentNo, 
				@RequestParam(name="greatNo", required = false) Integer greatNo, 
				@RequestParam("content") String content,
				@RequestParam("writerId") String writerId,
	    		@RequestParam("greatCommentId") String greatCommentId) throws IOException{
			
			PBoardComment comment = new PBoardComment();
			comment.setContent(content);
			
			PartyBoard pBoard = partyBoardService.getPBoardByNo(no);
			comment.setBoard(pBoard);
			
			if (parentNo != null) {
				PBoardComment parentComment = PBoardComment.builder().no(parentNo).build();
				comment.setParent(parentComment);
			}
			User writer = User.builder().id(greatCommentId).build();
			if (greatNo != null) {
				PBoardComment greatComment = PBoardComment.builder().user(writer).no(greatNo).build();
				comment.setGreat(greatComment);
			}
			User user = User.builder().id(id).build();
			comment.setUser(user);
			
			partyBoardService.insertComment(comment, writerId);
			PartyBoard board = partyBoardService.getPBoardByNo(no);
			int commentCount = board.getCommentCount() + 1;
			partyBoardService.updateBoardComment(no, commentCount);
			
			List<PBoardComment> parents = partyBoardService.getGreatComments(no);
			List<PBoardComment> childs = partyBoardService.getchildComments(no);
			
			PBoardCommentList list = PBoardCommentList
					.builder()
					.parentComments(parents)
					.childComments(childs)
					.build();
			
			return ResponseEntity.ok().body(list);
		}
		
		@PostMapping("/deleteGreatComment")
		@ResponseBody
		public ResponseEntity<Integer> deleteGreatComment(@RequestBody Map<String, Integer> request) {
			int no = request.get("no");
			int commentNo = request.get("greatCommentNo");
			if(no == 0 || commentNo == 0) {
				return ResponseEntity.badRequest().build();// 값이 없는 경우 잘못된 요청 응답 반환
			}
			
			// table의 commentCount 구하기
			PartyBoard board = partyBoardService.getPBoardByNo(no);
			// commentNo를 조상으로 갖고 있는 자손 댓글의 수 구하기
			int childCount = partyBoardService.getTotalChildCount(commentNo);
			// update할 commentCount 구하기
			int commentCount = board.getCommentCount() - (childCount + 1);
			
			// commentCount update
			board.setCommentCount(commentCount);
			partyBoardService.updateBoardComment(no, commentCount);
			
			// 자손 댓글 삭제
			partyBoardService.deleteChildComments(commentNo);
			
			// 해당 댓글 삭제
			partyBoardService.deleteComment(commentNo);
			
			return ResponseEntity.ok().body(commentCount);
		}
	    
		@PostMapping("/deleteReComment")
		@ResponseBody
		public ResponseEntity<Integer> deleteReComment(@RequestBody Map<String, Integer> request) {
			int no = request.get("no");
			int commentNo = request.get("commentNo");
			if(no == 0 || commentNo == 0) {
				return ResponseEntity.badRequest().build();// 값이 없는 경우 잘못된 요청 응답 반환
			}
			
			// table의 commentCount 구하기
			PartyBoard board = partyBoardService.getPBoardByNo(no);
			// update할 commentCount 구하기
			int commentCount = board.getCommentCount() - 1;
			
			// commentCount update
			board.setCommentCount(commentCount);
			partyBoardService.updateBoardComment(no, commentCount);
			
			// 해당 댓글 삭제
			partyBoardService.deleteComment(commentNo);
			
			return ResponseEntity.ok().body(commentCount);
		}
}
