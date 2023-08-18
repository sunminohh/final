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

import kr.co.mgv.board.list.StoreBoardList;
import kr.co.mgv.board.service.MovieBoardService;
import kr.co.mgv.board.service.StoreBoardService;
import kr.co.mgv.board.vo.BoardProduct;
import kr.co.mgv.board.vo.ReportReason;
import kr.co.mgv.board.vo.SBoardLike;
import kr.co.mgv.board.vo.StoreBoard;
import kr.co.mgv.board.vo.TBoardLike;
import kr.co.mgv.board.vo.TheaterBoard;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board/store")
@RequiredArgsConstructor
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
		
		// model에 조회한 리스트 담기
		model.addAttribute("result", result);
		
        return "/view/board/store/list";
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
    	
    	// 자손댓글 목록
    	
    	// 신고이유 목록
		List<ReportReason> reportReasons = movieBoardService.getReportReason();
		model.addAttribute("reasons", reportReasons);
    	
    	return "/view/board/store/detail";
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
    
    // 게시물 CRUD 관련
    @GetMapping("/add")
    public String storeForm() {
        return "/view/board/store/form";
    }
    

}
