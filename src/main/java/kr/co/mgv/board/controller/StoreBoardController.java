package kr.co.mgv.board.controller;

import java.util.ArrayList;
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

import kr.co.mgv.board.form.AddSboardForm;
import kr.co.mgv.board.form.ReportForm;
import kr.co.mgv.board.list.SBoardCommentList;
import kr.co.mgv.board.list.StoreBoardList;
import kr.co.mgv.board.service.MovieBoardService;
import kr.co.mgv.board.service.StoreBoardService;
import kr.co.mgv.board.vo.BoardCategory;
import kr.co.mgv.board.vo.BoardProduct;
import kr.co.mgv.board.vo.ReportReason;
import kr.co.mgv.board.vo.SBoardComment;
import kr.co.mgv.board.vo.SBoardLike;
import kr.co.mgv.board.vo.SboardReport;
import kr.co.mgv.board.vo.StoreBoard;
import kr.co.mgv.board.vo.TboardReport;
import kr.co.mgv.board.vo.TheaterBoard;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/board/store")
@RequiredArgsConstructor
@Slf4j
public class StoreBoardController {

	private final StoreBoardService storeBoardService;
	private final MovieBoardService movieBoardService;
	
    @GetMapping("/list")
    public String storeList(@RequestParam(name = "sort", required = false, defaultValue = "id") String sort,
			@RequestParam(name = "rows", required = false, defaultValue = "10") int rows,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "opt", required = false, defaultValue = "") String opt,
			@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(name = "productNo", required = false) Integer productNo,
			@RequestParam(name = "catNo", required = false) Integer catNo,
			@AuthenticationPrincipal User user,
			Model model) {
    	
    	Map<String , Object> param = new HashMap<String, Object>();
    	param.put("sort", sort);
    	param.put("rows", rows);
    	param.put("page", page);
		if (productNo != null) {
			param.put("productNo", productNo);
		}
		if (catNo != null) {
			param.put("catNo", catNo);
			List<BoardProduct> products = storeBoardService.getProductsByCatNo(catNo);
			model.addAttribute("products", products);
		}
		if(StringUtils.hasText(opt) && StringUtils.hasText(keyword)) {
			param.put("opt", opt);
			param.put("keyword", keyword);
		}
    	
		// service로 스토어게시물 목록, 카테고리/상품 목록, 페이지네이션 조회하기
		StoreBoardList result = storeBoardService.getSBoards(param);
		
		if(user != null) {
			// 로그인한 사용자의 신고 기록 가져오기
			List<SboardReport> reportList = storeBoardService.getReportById(user.getId());
			
			// 사용자가 신고한 게시물 제외
			List<StoreBoard> StoreBoardsToShow = new ArrayList<>();
			
			for(StoreBoard board : result.getStoreBoards()) {
				boolean isReported = reportList.stream()
						.anyMatch(report -> report.getBoard().getNo() == board.getNo());
				if(!isReported) {
					StoreBoardsToShow.add(board);
				}
			}
			result.setStoreBoards(StoreBoardsToShow);
			model.addAttribute("reports", StoreBoardsToShow);
		}
		
		// model에 조회한 리스트 담기
		model.addAttribute("result", result);
		
        return "view/board/store/list";
    }

    @GetMapping("/productByCatNo")
    @ResponseBody
    public List<BoardProduct> getProductByCatNo(@RequestParam("catNo") int catNo) {
    	List<BoardProduct> products = storeBoardService.getProductsByCatNo(catNo);
    	return products;
    }

    // 상세페이지 관련
    @GetMapping("/read")
    public String read(@RequestParam("no") int no,
					   @RequestParam("page") int page,
					   @RequestParam(name = "rows", required = false, defaultValue = "10") Integer rows,
					   @RequestParam("sort") String sort,
					   @RequestParam(name = "productNo", required = false) Integer productNo,
					   @RequestParam("opt") String opt,
					   @RequestParam("keyword") String keyword,
					   RedirectAttributes redirectAttributes) {
    	
    	storeBoardService.increseRead(no);
    	
		
		redirectAttributes.addAttribute("no", no);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("sort", sort);
        if(rows != null) {
        	redirectAttributes.addAttribute("rows", rows);		
        }
        redirectAttributes.addAttribute("productNo", productNo);
        redirectAttributes.addAttribute("opt", opt);
        redirectAttributes.addAttribute("keyword", keyword);
    	
    	return "redirect:/board/store/detail";
    }

    @GetMapping("/detail")
    public String storeDetail(@RequestParam("no") int no,
    						  @AuthenticationPrincipal User user,
    						  Model model) {
    	
    	// 로그인한 상태일때 해당 유저의 좋아요 정보를 조회하여 전달한다.
    	if(user != null) {
    		StoreBoard board = StoreBoard.builder().no(no).build();
    		SBoardLike like = SBoardLike.builder().user(user).board(board).build();
    		SBoardLike savedLike = storeBoardService.getLike(like);
    		model.addAttribute("like", savedLike);
    	}
    	
    	// 게시물 번호로 게시물 조회
    	StoreBoard storeBoard = storeBoardService.getStoreBoardByNo(no);
    	model.addAttribute("board", storeBoard);
    	// 모댓글 목록
    	List<SBoardComment> comments = storeBoardService.getComments(no);
    	model.addAttribute("comments", comments);
    	// 자손댓글 목록
    	List<SBoardComment> childComments = storeBoardService.getChildComments(no);
    	model.addAttribute("childComments", childComments);
    	// 신고이유 목록
		List<ReportReason> reportReasons = movieBoardService.getReportReason();
		model.addAttribute("reasons", reportReasons);
    	
    	return "view/board/store/detail";
    }

    // 좋아요 관련
	@PostMapping("/changelike")
	@ResponseBody
	public ResponseEntity<Void> addLike(@RequestParam("no") int no,
										@RequestParam("id") String id,
										@RequestParam("likeCount") int likeCount){
		
		SBoardLike like = new SBoardLike();
    	User user = User.builder()
				.id(id)
				.build();
    	like.setUser(user);
    	StoreBoard board = StoreBoard.builder()
						.no(no)
						.build();
		like.setBoard(board); 
		SBoardLike savedLike = storeBoardService.getLike(like);
		
	 	if (savedLike != null && "Y".equals(savedLike.getCancel())) {
    		savedLike.setCancel("N");
    		storeBoardService.updateSboardLike(savedLike);
    	} else if(savedLike != null && "N".equals(savedLike.getCancel())) {
    		savedLike.setCancel("Y");
    		storeBoardService.updateSboardLike(savedLike);
    	} else if(savedLike == null) {
    		storeBoardService.insertBoardLike(like);
    	}
	 	storeBoardService.updateBoardLike(no, likeCount);
	 	
	 	return ResponseEntity.ok().build();
	}    
    
    // 댓글 관련
	@PostMapping("/addComment")
	@ResponseBody
	public ResponseEntity<SBoardCommentList> addComment (@RequestParam("no") int no, 
										             @RequestParam("id") String id, 
										             @RequestParam(name="parentNo", required = false) Integer parentNo, 
										             @RequestParam(name="greatNo", required = false) Integer greatNo, 
										             @RequestParam("content") String content){
		SBoardComment comment = new SBoardComment();
		comment.setContent(content);
		
		StoreBoard sBoard = StoreBoard.builder().no(no).build();
		comment.setBoard(sBoard);
		
		if (parentNo != null) {
		SBoardComment parentComment = SBoardComment.builder().no(parentNo).build();
		comment.setParent(parentComment);
		}
		if (greatNo != null) {
		SBoardComment greatComment = SBoardComment.builder().no(greatNo).build();
		comment.setGreat(greatComment);
		}
		
		User user = User.builder().id(id).build();
		comment.setUser(user);

		storeBoardService.SBoardCommentInsert(comment);
		StoreBoard board = storeBoardService.getStoreBoardByNo(no);
		int commentCount = board.getCommentCount() + 1;
		storeBoardService.updateBoardComment(no, commentCount);
		
		List<SBoardComment> parents = storeBoardService.getComments(no);
		List<SBoardComment> childs = storeBoardService.getChildComments(no);
		SBoardCommentList list = SBoardCommentList
								 .builder()
								 .parentComments(parents)
								 .childComments(childs)
								 .build(); 		
		return ResponseEntity.ok().body(list);
	}
	
	@PostMapping("/addReComment")
	@ResponseBody
	public ResponseEntity<SBoardCommentList> addReComment (@RequestParam("no") int no, 
										             @RequestParam("id") String id, 
										             @RequestParam(name="parentNo", required = false) Integer parentNo, 
										             @RequestParam(name="greatNo", required = false) Integer greatNo, 
										             @RequestParam("content") String content){
		SBoardComment comment = new SBoardComment();
		comment.setContent(content);
		
		StoreBoard sBoard = StoreBoard.builder().no(no).build();
		comment.setBoard(sBoard);
		
		if (parentNo != null) {
		SBoardComment parentComment = SBoardComment.builder().no(parentNo).build();
		comment.setParent(parentComment);
		}
		if (greatNo != null) {
		SBoardComment greatComment = SBoardComment.builder().no(greatNo).build();
		comment.setGreat(greatComment);
		}
		
		User user = User.builder().id(id).build();
		comment.setUser(user);

		storeBoardService.SBoardCommentInsert(comment);
		StoreBoard board = storeBoardService.getStoreBoardByNo(no);
		int commentCount = board.getCommentCount() + 1;
		storeBoardService.updateBoardComment(no, commentCount);
		
		List<SBoardComment> parents = storeBoardService.getComments(no);
		List<SBoardComment> childs = storeBoardService.getChildComments(no);
		SBoardCommentList list = SBoardCommentList
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
		StoreBoard board = storeBoardService.getStoreBoardByNo(no);
		// commentNo를 조상으로 갖고 있는 자손 댓글의 수 구하기
		int childCount = storeBoardService.getTotalChildCount(commentNo);
		// update할 commentCount 구하기
		int commentCount = board.getCommentCount() - (childCount + 1);
		
		// commentCount update
		board.setCommentCount(commentCount);
		storeBoardService.updateBoardComment(no, commentCount);
		
		// 자손 댓글 삭제
		storeBoardService.childCommentDelete(commentNo);
		
		// 해당 댓글 삭제
		storeBoardService.greatCommentDelete(commentNo);
		
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
		StoreBoard board = storeBoardService.getStoreBoardByNo(no);
		// update할 commentCount 구하기
		int commentCount = board.getCommentCount() - 1;
		
		// commentCount update
		board.setCommentCount(commentCount);
		storeBoardService.updateBoardComment(no, commentCount);
		
		// 해당 댓글 삭제
		storeBoardService.greatCommentDelete(commentNo);
		
		return ResponseEntity.ok().body(commentCount);
	}
	
    // 게시물 CRUD 관련
    @GetMapping("/add")
    public String storeForm(Model model) {
    	List<BoardCategory> categories = storeBoardService.getCategories();
    	model.addAttribute("categories", categories);
    	
        return "view/board/store/form";
    }
    
    @PostMapping("/add")
    public String addStoreBoard(@AuthenticationPrincipal User user, AddSboardForm form) {
    	storeBoardService.addSboard(form, user);
    	
    	return "redirect:/board/store/list";
    }
    
    @GetMapping("/modify")
    public String storeBoardModifyForm(@RequestParam("no") int no, Model model) {
    	List<BoardCategory> categories = storeBoardService.getCategories();
    	model.addAttribute("categories", categories);
    	
    	StoreBoard savedBoard = storeBoardService.getStoreBoardByNo(no);
    	model.addAttribute("board", savedBoard);
    	
    	return "view/board/store/modifyForm";
    }
    
    @PostMapping("/modify")
    public String modifyBoard(@RequestParam("no") int no, AddSboardForm form) {
    	storeBoardService.updateSBoard(form, no);
    	return "redirect:/board/store/detail?no=" + no;
    }
    
    @GetMapping("/delete")
    public String deleteBoard(@RequestParam("no") int no) {
    	storeBoardService.deleteBoard(no);
    	return "redirect:/board/store/list";
    }
    
	// 신고관련 
	@PostMapping("/report")
	public String reportBoard(ReportForm form, @AuthenticationPrincipal User user) {
    	log.info("입력 내용 -> {}", form.getReasonNo());
    	log.info("입력 내용 -> {}", form.getBoardNo());
    	log.info("입력 내용 -> {}", form.getReasonContent());
    	log.info("입력 내용 -> {}", user.getId());
		storeBoardService.insertReport(form, user);
		
		StoreBoard board = storeBoardService.getStoreBoardByNo(form.getBoardNo());
		int reportCount = board.getReportCount()+1;
		storeBoardService.updateReportCount(form.getBoardNo(), reportCount);
		
		if (reportCount == 5) {
			String report = "Y";
			storeBoardService.updateReport(form.getBoardNo(), report);
		}
		return "redirect:/board/store/list";
	}
}
