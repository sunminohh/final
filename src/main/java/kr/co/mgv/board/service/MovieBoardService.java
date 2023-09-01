package kr.co.mgv.board.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.mgv.board.BoardPagination;
import kr.co.mgv.board.form.AddBoardNoticeForm;
import kr.co.mgv.board.form.AddMboardForm;
import kr.co.mgv.board.form.MBoardForm;
import kr.co.mgv.board.form.ReportForm;
import kr.co.mgv.board.list.MovieBoardList;
import kr.co.mgv.board.mapper.BoardNoticeDao;
import kr.co.mgv.board.mapper.MovieBoardDao;
import kr.co.mgv.board.vo.MBoardComment;
import kr.co.mgv.board.vo.MBoardLike;
import kr.co.mgv.board.vo.MboardReport;
import kr.co.mgv.board.vo.MovieBoard;
import kr.co.mgv.board.vo.ReportReason;
import kr.co.mgv.board.websocket.handler.NoticeWebsocketHandler;
import kr.co.mgv.movie.vo.Movie;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieBoardService {
	
	private final MovieBoardDao movieBoardDao;
	private final NoticeWebsocketHandler noticeWebsocketHandler;
	private final BoardNoticeDao boardNoticeDao;

// 게시물 리스트
	public MovieBoardList getMBoards(Map<String, Object> param) {
		
		// pagination
		int totalRows = movieBoardDao.getTotalRows(param);
		
		int page = (int) param.get("page");
		int rows = (int) param.get("rows");
		BoardPagination pagination = new BoardPagination(rows, page, totalRows);
		int begin = pagination.getBegin();
		int end = pagination.getEnd();
		
		param.put("begin", begin);
		param.put("end",end);
		
		// 게시판 목록
		List<MovieBoard> movieBoards = movieBoardDao.getMBoards(param);
		
		// MovieBoardList에 pagination과 게시판 목록을 담는다.
		MovieBoardList result = new MovieBoardList();
		result.setPagination(pagination);
		result.setMovieBoards(movieBoards);
		
		return result;
	}

// 상세페이지
	public MovieBoard getMovieBoardByNo(int no) {
		MovieBoard movieBoard = movieBoardDao.getMBoardByNo(no);
		
		return movieBoard;
	}

	public void increaseRead(int no) {
		MovieBoard movieBoard = movieBoardDao.getMBoardByNo(no);
		movieBoard.setReadCount(movieBoard.getReadCount() + 1);
		movieBoardDao.updateMBoardByNo(movieBoard);

	}
	
	public void updateBoardLike(int no, int likeCount) {
		MovieBoard movieBoard = movieBoardDao.getMBoardByNo(no);
		movieBoard.setLikeCount(likeCount);
		
		movieBoardDao.updateMBoardByNo(movieBoard);
		
	}
	
	public void insertBoardLike(MBoardLike like) {
		movieBoardDao.insertMBoardLike(like);
	}
	
	public MBoardLike getLike(MBoardLike like) {
		return movieBoardDao.getLikeByBnoAndId(like);
	}
	
	public void updateMBoardLike (MBoardLike like ,String writerId) throws IOException {
		
		MBoardLike savedLike = movieBoardDao.getLikeByBnoAndId(like);

		String fromId = like.getUser().getId();
		String type = "영화";
		int boardNo = like.getBoard().getNo();
		MovieBoard board = movieBoardDao.getMBoardByNo(boardNo);
		String BoardName = board.getName();
		
    	if(savedLike != null && "Y".equals(savedLike.getCancel()) && !writerId.equals(fromId)) {
    		String text = "["+ type + "]게시판 [" +BoardName+ "...]에 " + fromId + "님이 게시글을 좋아합니다."+boardNo; 
			noticeWebsocketHandler.sendMessage(writerId, text);
			log.info("text -> {}",text);
			AddBoardNoticeForm form = AddBoardNoticeForm.builder()
					  .boardType(type)
					  .boardNo(boardNo)
					  .fromId(fromId)
					  .toId(writerId)
					  .code("like")
					  .build();
			boardNoticeDao.insertNotice(form);
    	} 
    	
    	if(savedLike == null && !writerId.equals(fromId)) {
    		String text = "["+ type + "]게시판 [" +BoardName+ "...]에 " + fromId + "님이 게시글을 좋아합니다."+boardNo; 
			noticeWebsocketHandler.sendMessage(writerId, text);
			log.info("text -> {}",text);
			AddBoardNoticeForm form = AddBoardNoticeForm.builder()
					  .boardType(type)
					  .boardNo(boardNo)
					  .fromId(fromId)
					  .toId(writerId)
					  .code("like")
					  .build();
			boardNoticeDao.insertNotice(form);
    	}
		
		movieBoardDao.updateLike(like);
	}
	
	public void updateBoardComment(int no, int commentCount) {
		MovieBoard movieBoard = movieBoardDao.getMBoardByNo(no);
		movieBoard.setCommentCount(commentCount);
		
		movieBoardDao.updateMBoardByNo(movieBoard);
		
	}
	
	
// comment
	
	public void MBoardCommentInsert(MBoardComment comment,  String writerId) throws IOException {
		movieBoardDao.insertMBoardComment(comment);
		
		String type = "영화";
		String code = "comment";
		int boardNo = comment.getBoard().getNo();
		String boardName = comment.getBoard().getName();
		if (boardName.length() > 8) {
			boardName =  boardName.substring(0, 8);
		}
		String fromId = comment.getUser().getId();
		// 댓글 달렸을때, 댓글작성자 -> 게시물 작성자
		if(!fromId.equals(writerId) && comment.getGreat() == null) {
			log.info("게시글작성자-> {}",writerId);
			log.info("현댓글작성자-> {}",fromId);
			String text = "["+ type + "]게시판 [" +boardName+ "...]에 " + fromId + "님이 댓글을 달았습니다."+boardNo; 
			noticeWebsocketHandler.sendMessage(writerId, text);
			log.info("text -> {}",text);
			
			AddBoardNoticeForm form = AddBoardNoticeForm.builder()
									  .boardType(type)
									  .boardNo(boardNo)
									  .fromId(fromId)
									  .toId(writerId)
									  .code("comment")
									  .build();
			boardNoticeDao.insertNotice(form);
		}
		
		// 대댓글 달렸을때, 대댓글 작성자 -> 댓글 작성자
		if(comment.getGreat() != null && ! comment.getGreat().getUser().getId().equals(fromId)) {
				log.info("게시글 작성자-> {}",writerId);
				log.info("현댓글 작성자-> {}",fromId);
				log.info("모댓글 작성자 -> {}",comment.getGreat().getUser().getId());
				String text = "["+ type + "]게시판 [" +boardName+ "...]에 " + fromId + "님이 대댓글을 달았습니다."+boardNo; 
				noticeWebsocketHandler.sendMessage(comment.getGreat().getUser().getId(), text);
				log.info("text -> {}",text);
				
				AddBoardNoticeForm form = AddBoardNoticeForm.builder()
						  .boardType(type)
						  .boardNo(boardNo)
						  .fromId(fromId)
						  .toId(comment.getGreat().getUser().getId())
						  .code("reComment")
						  .build();
				boardNoticeDao.insertNotice(form);
		}
		
		// 내 게시글의 다른 사용자의 댓글에 내가 아닌 사용자가 대댓글을 달았다
		if(comment.getGreat() != null &&  comment.getGreat().getUser().getId().equals(fromId)) {
			log.info("게시글 작성자-> {}",writerId);
			log.info("현댓글 작성자-> {}",fromId);
			log.info("모댓글 작성자 -> {}",comment.getGreat().getUser().getId());
			String text = "["+ type + "]게시판 [" +boardName+ "...]에 " + fromId + "님이 댓글을 달았습니다."+boardNo; 
			noticeWebsocketHandler.sendMessage(writerId, text);
			log.info("text -> {}",text);
			
			AddBoardNoticeForm form = AddBoardNoticeForm.builder()
					  .boardType(type)
					  .boardNo(boardNo)
					  .fromId(fromId)
					  .toId(writerId)
					  .code("comment")
					  .build();
			boardNoticeDao.insertNotice(form);
		}
	}
	
	public List<MBoardComment> getComments(int no) {
		List<MBoardComment> comments = movieBoardDao.getMBoardComments(no);
		
		return comments;
	}
	
	public List<MBoardComment> getChildComments(int no) {
		List<MBoardComment> childComments = movieBoardDao.getMBoardChildComments(no);
		return childComments;
	}
	
	public MBoardComment getGreatComment(int no, String id) {
		User user = User.builder()
				.id(id)
				.build();
		
		MovieBoard board = MovieBoard.builder()
						.no(no)
						.build();
		MBoardComment comment = MBoardComment.builder()
					.user(user)
					.board(board)
					.build();
		
		return movieBoardDao.getGreatComment(comment);
		
	}
	
	public MBoardComment getChildComment(int no, String id) {
		
		User user = User.builder()
				.id(id)
				.build();
		
		MovieBoard board = MovieBoard.builder()
						.no(no)
						.build();
		MBoardComment comment = MBoardComment.builder()
					.user(user)
					.board(board)
					.build();
		
		return movieBoardDao.getChildComment(comment);
	}
	
	public void greatCommentDelete (int no) {
		movieBoardDao.deleteGreatComment(no);
	}
	
	public void childsCommentDelete (int greatNo) {
		movieBoardDao.deleteChildsComment(greatNo);
	}
	
	public int gatTotalChildCount (int no) {
		return movieBoardDao.getTotalCommentCount(no);
	}

	// 게시물 등록 관련
	public List<Movie> getMovieTitle() {
		return movieBoardDao.getMovieTitle();
	} 
	
	public void addMBoard(AddMboardForm form, User user) {
	    try {
	    	Movie movie = Movie.builder()
	    					.no(form.getMovieNo())
	    					.build();
	        MovieBoard board = MovieBoard.builder()
	        					.user(user)
	        					.movie(movie)
	        					.name(form.getName())
	        					.fileName(form.getFileName())
	        					.content(form.getContent())
	        					.build();

	        movieBoardDao.insertMBoard(board);            
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void updateMBoard(AddMboardForm form, int no) {
	    try {
	    	Movie movie = Movie.builder()
	    					.no(form.getMovieNo())
	    					.build();
	        MovieBoard board = MovieBoard.builder()
	        					.no(no)
	        					.movie(movie)
	        					.name(form.getName())
	        					.fileName(form.getFileName())
	        					.content(form.getContent())
	        					.build();

	        movieBoardDao.updateMBoardByNo(board);         
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void deleteBoard(int no, MBoardForm form) {
		MovieBoard movieBoard = movieBoardDao.getMBoardByNo(no);
		movieBoard.setDeleted(form.getDeleted());
		
		movieBoardDao.updateMBoardByNo(movieBoard);
		
	}
	
	// 신고 관련
	public List<ReportReason> getReportReason() {
		return movieBoardDao.getReportReason();
	}
	
	public void insertReport (ReportForm form, User user) {
		
	    try {
	    	MovieBoard board = movieBoardDao.getMBoardByNo(form.getBoardNo());
	    	ReportReason reason = ReportReason.builder().no(form.getReasonNo()).build();
	    	
	    	MboardReport report = MboardReport.builder()
	    							.reasonContent(form.getReasonContent())
	    							.board(board)
	    							.user(user)
	    							.reason(reason)
	    							.build();
	    	movieBoardDao.insertMboardReport(report);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void updateReportCount (int no, int reportCount) {
		MovieBoard board = movieBoardDao.getMBoardByNo(no);
		board.setReportCount(reportCount);
		movieBoardDao.updateMBoardByNo(board);
	}
	
	public void updateReport (int no, String report) {
		MovieBoard board = movieBoardDao.getMBoardByNo(no);
		board.setReport(report);
		movieBoardDao.updateMBoardByNo(board);
	}
	
	public List<MboardReport> getReportById (String id) {
		return movieBoardDao.getMboardReportById(id);
	}
}
