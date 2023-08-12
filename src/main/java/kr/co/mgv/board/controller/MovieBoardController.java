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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.mgv.board.list.MboardCommentList;
import kr.co.mgv.board.list.MovieBoardList;
import kr.co.mgv.board.service.MovieBoardService;
import kr.co.mgv.board.vo.MBoardComment;
import kr.co.mgv.board.vo.MBoardLike;
import kr.co.mgv.board.vo.MovieBoard;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/board/movie")
@RequiredArgsConstructor
@Slf4j
public class MovieBoardController {

	private final MovieBoardService movieBoardService;
	
	// 게시물 리스트 관련
    @GetMapping("/list")
    public String movieList(@RequestParam(name = "sort", required = false, defaultValue = "id") String sort,
			@RequestParam(name = "rows", required = false, defaultValue = "10") int rows,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "opt", required = false, defaultValue = "") String opt,
			@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
			Model model) {
    	
    	Map<String, Object> param = new HashMap<String, Object>();
		param.put("sort", sort);
		param.put("rows", rows);
		param.put("page", page);

		if(StringUtils.hasText(opt) && StringUtils.hasText(keyword)) {
			param.put("opt", opt);
			param.put("keyword", keyword);
		}
    	
		// service로 영화게시물 목록 조회하기 
		MovieBoardList result = movieBoardService.getMBoards(param);
		
		// model에 조회한 극장게시물 담기
		model.addAttribute("result", result);
		
        return "/view/board/movie/list";
    }

    // 상세페이지 관련
    @GetMapping("/read")
    public String read(@RequestParam("no") int no,
    				   @RequestParam("page") int page,
    				   @RequestParam(name = "rows", required = false, defaultValue = "10") Integer rows,
    				   @RequestParam("sort") String sort,
    				   @RequestParam("opt") String opt,
    				   @RequestParam("keyword") String keyword,
    				   RedirectAttributes redirectAttributes) {
    	
    	movieBoardService.increaseRead(no);
    	
    	
    	redirectAttributes.addAttribute("no", no);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("sort", sort);
        if(rows != null) {
        	redirectAttributes.addAttribute("rows", rows);		
        }
        redirectAttributes.addAttribute("opt", opt);
        redirectAttributes.addAttribute("keyword", keyword);

        
        return "redirect:/board/movie/detail";
    }
    
    @GetMapping("/detail")
    public String theaterDetail(@RequestParam("no") int no,
    							Model model,
    							@AuthenticationPrincipal User user) {
    	if(user != null) {
    		MBoardLike like = new MBoardLike();
        	like.setUser(user);
        	MovieBoard board = MovieBoard.builder()
        						.no(no)
        						.build();
        	like.setBoard(board);
    		
    		MBoardLike savedLike = movieBoardService.getLike(like);
    		model.addAttribute("like", savedLike);
    	}
    	MovieBoard movieBoard = movieBoardService.getMovieBoardByNo(no);
    	List<MBoardComment> comments = movieBoardService.getComments(no);
    	List<MBoardComment> childComments = movieBoardService.getChildComments(no);
    	model.addAttribute("comments", comments);
    	model.addAttribute("childComments", childComments);
    	model.addAttribute("board", movieBoard);
    	

    	
        return "/view/board/movie/detail";
    }

    @GetMapping("/add")
    public String theaterForm() {
        return "/view/board/movie/form";
    }
    
    
    @PostMapping("/changelike")
    @ResponseBody
    public ResponseEntity<Void> addLike(@RequestParam("no") int no,
    									@RequestParam("id") String id,
    									@RequestParam("likeCount") int likeCount) {
				    	
    	MBoardLike like = new MBoardLike();
    	User user = User.builder()
    				.id(id)
    				.build();
    	like.setUser(user);
    	MovieBoard board = MovieBoard.builder()
    						.no(no)
    						.build();
    	like.setBoard(board);    	
    	MBoardLike savedLike = movieBoardService.getLike(like);

    	if (savedLike != null && "Y".equals(savedLike.getCancel())) {
    		savedLike.setCancel("N");
    		movieBoardService.updateMBoardLike(savedLike);
    	} else if(savedLike != null && "N".equals(savedLike.getCancel())) {
    		savedLike.setCancel("Y");
    		movieBoardService.updateMBoardLike(savedLike);
    	} else if(savedLike == null) {
    		movieBoardService.insertBoardLike(like);
    	}
    	movieBoardService.updateBoardLike(no, likeCount);
    	

    	return ResponseEntity.ok().build();
    }
        

    // 댓글 관련
    @PostMapping("/addComment")
    @ResponseBody
    public ResponseEntity<MBoardComment> addComment(@RequestParam("no") int no, 
                             @RequestParam("id") String id, 
                             @RequestParam(name="parentNo", required = false) Integer parentNo, 
                             @RequestParam(name="greatNo", required = false) Integer greatNo, 
                             @RequestParam("content") String content) {
        
    	log.info("게시물 번호 -> {}", no);
    	log.info("사용자 아이디 -> {}", id);
    	log.info("내용 -> {}", content);
    	log.info("부모번호 -> {}", parentNo);
    	log.info("조상번호 -> {}", greatNo);
    	
    	MBoardComment comment = new MBoardComment();
    	comment.setContent(content);
    	
    	MovieBoard mBoard = MovieBoard.builder()
    						.no(no)
    						.build();
    	comment.setBoard(mBoard);
    	
		if (parentNo != null) {
			MBoardComment parentComment = MBoardComment.builder()
					.no(parentNo)
					.build();
			comment.setParent(parentComment);
		}
		if (greatNo != null) {
			MBoardComment greatComment = MBoardComment.builder()
					.no(greatNo)
					.build();
			comment.setGreat(greatComment);
		}
    	
    	User user = User.builder()
    			.id(id)
    			.build();
    	comment.setUser(user);
    	
    	movieBoardService.MBoardCommentInsert(comment);
    	MovieBoard board = movieBoardService.getMovieBoardByNo(no);
    	int commentCount = board.getCommentCount()+1;
    	
    	movieBoardService.updateBoardComment(no, commentCount);

    	MBoardComment inputComment = movieBoardService.getGreatComment(no, id);
    	
        return ResponseEntity.ok().body(inputComment);
    }

    @PostMapping("/addReComment")
    @ResponseBody
    public ResponseEntity<MBoardComment> addReComment(@RequestParam("no") int no, 
    		@RequestParam("id") String id, 
    		@RequestParam(name="parentNo", required = false) Integer parentNo, 
    		@RequestParam(name="greatNo", required = false) Integer greatNo, 
    		@RequestParam("content") String content) {
    	
    	log.info("게시물 번호 -> {}", no);
    	log.info("사용자 아이디 -> {}", id);
    	log.info("내용 -> {}", content);
    	log.info("부모번호 -> {}", parentNo);
    	log.info("조상번호 -> {}", greatNo);
    	
    	MBoardComment comment = new MBoardComment();
    	comment.setContent(content);
    	
    	MovieBoard mBoard = MovieBoard.builder()
    			.no(no)
    			.build();
    	comment.setBoard(mBoard);
    	
    	if (parentNo != null) {
    		MBoardComment parentComment = MBoardComment.builder()
    				.no(parentNo)
    				.build();
    		comment.setParent(parentComment);
    	}
    	if (greatNo != null) {
    		MBoardComment greatComment = MBoardComment.builder()
    				.no(greatNo)
    				.build();
    		comment.setGreat(greatComment);
    	}
    	
    	User user = User.builder()
    			.id(id)
    			.build();
    	comment.setUser(user);
    	
    	movieBoardService.MBoardCommentInsert(comment);
    	MovieBoard board = movieBoardService.getMovieBoardByNo(no);
    	int commentCount = board.getCommentCount()+1;
    	
    	movieBoardService.updateBoardComment(no, commentCount);
    	
    	MBoardComment inputComment = movieBoardService.getChildComment(no, id);
    	
    	return ResponseEntity.ok().body(inputComment);
    }
    

    @PostMapping("/deleteComment")
    @ResponseBody
    public ResponseEntity<Void> deleteComment(@RequestParam("no") int no, 
					    		@RequestParam("commentNo") int commentNo) {
    	
    	// comment table의 deleted 를 y로 바꾸기
    	// table의 commentCount -1
    		// great_no == null이면 great_no == comment_no 인 comment 전부 y로 바꾸기
    		// great_no == null이면 great_no == comment_no 인 comment 인 수만큼 commentCount - 하기
    	
    	
        
    	return ResponseEntity.ok().build();
    }

}
