package kr.co.mgv.board.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.mgv.board.BoardPagination;
import kr.co.mgv.board.form.AddBoardNoticeForm;
import kr.co.mgv.board.form.AddSboardForm;
import kr.co.mgv.board.form.ReportForm;
import kr.co.mgv.board.list.StoreBoardList;
import kr.co.mgv.board.mapper.BoardNoticeDao;
import kr.co.mgv.board.mapper.StoreBoardDao;
import kr.co.mgv.board.vo.BoardCategory;
import kr.co.mgv.board.vo.BoardProduct;
import kr.co.mgv.board.vo.MovieBoard;
import kr.co.mgv.board.vo.ReportReason;
import kr.co.mgv.board.vo.SBoardComment;
import kr.co.mgv.board.vo.SBoardLike;
import kr.co.mgv.board.vo.SboardReport;
import kr.co.mgv.board.vo.StoreBoard;
import kr.co.mgv.board.websocket.handler.NoticeWebsocketHandler;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreBoardService {

	private final StoreBoardDao storeBoardDao;
	private final NoticeWebsocketHandler noticeWebsocketHandler;
	private final BoardNoticeDao boardNoticeDao;

	
	public StoreBoardList getSBoards(Map<String, Object> param) {
		
		// pagination
		int totalRows = storeBoardDao.getTotalRows(param);
		
		int page = (int) param.get("page");
		int rows = (int) param.get("rows");
		BoardPagination pagination = new BoardPagination(rows, page, totalRows);
		int begin = pagination.getBegin();
		int end = pagination.getEnd();
		
		param.put("begin", begin);
		param.put("end", end);
		
		// 게시판 목록
		List<StoreBoard> storeBoards = storeBoardDao.getSBoards(param);
		
		// 카테고리 목록
		List<BoardCategory> categories = storeBoardDao.getCatetories();
		
		// 조회한 목록을 담을 list를 생성한다.
		StoreBoardList result = new StoreBoardList();
		
		// TheaterBoardList에 pagination과 게시판 목록, 카테고리 목록을 담는다.
		result.setCategories(categories);
		result.setPagination(pagination);
		result.setStoreBoards(storeBoards);
		
		return result;
	}
	
	public List<BoardCategory> getCategories(){
		return storeBoardDao.getCatetories();
	}
	
	public List<BoardProduct> getProductsByCatNo(int catNo){
		List<BoardProduct> products = storeBoardDao.getProductsByCatNo(catNo);
		return products;
	}
	
	// 상세페이지
	public void increseRead(int no) {
		StoreBoard board = storeBoardDao.getSBoardByNo(no);
		board.setReadCount(board.getReadCount()+1);
		storeBoardDao.updateSBoardByNo(board);
	}
	
	public StoreBoard getStoreBoardByNo (int no) {
		return storeBoardDao.getSBoardByNo(no);
	}
	
	public void updateBoardLike(int no, int likeCount) {
		StoreBoard board = storeBoardDao.getSBoardByNo(no);
		board.setLikeCount(likeCount);
		storeBoardDao.updateSBoardByNo(board);
	}
	
	public void insertBoardLike (SBoardLike like) {
		storeBoardDao.insertSBoardLike(like);
	}
	
	public SBoardLike getLike(SBoardLike like) {
		return storeBoardDao.getLikeByBnoAndId(like);
	}
	
	public void updateSboardLike(SBoardLike like, String writerId) throws IOException {
		
		SBoardLike savedLike = storeBoardDao.getLikeByBnoAndId(like);
		
		String fromId = like.getUser().getId();
		String type = "스토어";
		int boardNo = like.getBoard().getNo();
		StoreBoard board = storeBoardDao.getSBoardByNo(boardNo);
		String BoardName = board.getName();
		if (BoardName.length() > 8) {
			BoardName =  BoardName.substring(0, 8);
		}
		
    	if(savedLike != null && "Y".equals(savedLike.getCancel()) && !writerId.equals(fromId)) {
    		String text = "["+ type + "]게시판 [" +BoardName+ "...]에 " + fromId + "님이 게시글을 좋아합니다."+boardNo; 
			noticeWebsocketHandler.sendMessage(writerId, text);
			AddBoardNoticeForm form = AddBoardNoticeForm.builder()
					  .boardType(type)
					  .boardNo(boardNo)
					  .fromId(fromId)
					  .toId(writerId)
					  .code("like")
					  .boardName(BoardName)
					  .build();
			boardNoticeDao.insertNotice(form);
    	} 
    	
    	if(savedLike == null && !writerId.equals(fromId)) {
    		String text = "["+ type + "]게시판 [" +BoardName+ "...]에 " + fromId + "님이 게시글을 좋아합니다."+boardNo; 
			noticeWebsocketHandler.sendMessage(writerId, text);
			AddBoardNoticeForm form = AddBoardNoticeForm.builder()
					  .boardType(type)
					  .boardNo(boardNo)
					  .fromId(fromId)
					  .toId(writerId)
					  .code("like")
					  .boardName(BoardName)
					  .build();
			boardNoticeDao.insertNotice(form);
    	}
		
		storeBoardDao.updateLike(like);
	}

	// 댓글관련
	public void SBoardCommentInsert(SBoardComment comment, String writerId) throws IOException {
		storeBoardDao.insertSBoardComment(comment);
		
		String type = "스토어";
		String code = "comment";
		int boardNo = comment.getBoard().getNo();
		String boardName = comment.getBoard().getName();
		if (boardName.length() > 8) {
			boardName =  boardName.substring(0, 8);
		}
		String fromId = comment.getUser().getId();
		// 댓글 달렸을때, 댓글작성자 -> 게시물 작성자
		if(!fromId.equals(writerId) && comment.getGreat() == null) {
			String text = "["+ type + "]게시판 [" +boardName+ "...]에 " + fromId + "님이 댓글을 달았습니다."+boardNo; 
			noticeWebsocketHandler.sendMessage(writerId, text);
			
			AddBoardNoticeForm form = AddBoardNoticeForm.builder()
									  .boardType(type)
									  .boardNo(boardNo)
									  .fromId(fromId)
									  .toId(writerId)
									  .code("comment")
									  .boardName(boardName)
									  .build();
			boardNoticeDao.insertNotice(form);
		}
		
		// 대댓글 달렸을때, 대댓글 작성자 -> 댓글 작성자
		if(comment.getGreat() != null && ! comment.getGreat().getUser().getId().equals(fromId)) {
				String text = "["+ type + "]게시판 [" +boardName+ "...]에 " + fromId + "님이 대댓글을 달았습니다."+boardNo; 
				noticeWebsocketHandler.sendMessage(comment.getGreat().getUser().getId(), text);
				
				AddBoardNoticeForm form = AddBoardNoticeForm.builder()
						  .boardType(type)
						  .boardNo(boardNo)
						  .fromId(fromId)
						  .toId(comment.getGreat().getUser().getId())
						  .code("reComment")
						  .boardName(boardName)
						  .build();
				boardNoticeDao.insertNotice(form);
		}
		
		// 내 게시글의 다른 사용자의 댓글에 내가 아닌 사용자가 대댓글을 달았다
		if(comment.getGreat() != null &&  !writerId.equals(fromId) ) {
			String text = "["+ type + "]게시판 [" +boardName+ "...]에 " + fromId + "님이 댓글을 달았습니다."+boardNo; 
			noticeWebsocketHandler.sendMessage(writerId, text);
			
			AddBoardNoticeForm form = AddBoardNoticeForm.builder()
					  .boardType(type)
					  .boardNo(boardNo)
					  .fromId(fromId)
					  .toId(writerId)
					  .code("comment")
					  .boardName(boardName)
					  .build();
			boardNoticeDao.insertNotice(form);
		}
	}
	
	public void updateBoardComment(int no, int commentCount) {
		StoreBoard board = storeBoardDao.getSBoardByNo(no);
		board.setCommentCount(commentCount);
		storeBoardDao.updateSBoardByNo(board);
	}
	
	public List<SBoardComment> getComments(int no) {
		return storeBoardDao.getSBoardComments(no);
	}
	
	public List<SBoardComment> getChildComments(int no)	{
		return storeBoardDao.getSBoardChildComments(no);
	}
	
	public SBoardComment getGreatComment(int no, String id) {
		User user = User.builder().id(id).build();
		StoreBoard board = StoreBoard.builder().no(no).build();
		SBoardComment comment = SBoardComment.builder().user(user).board(board).build();
		return storeBoardDao.getGreatComment(comment);
	}
	
	public SBoardComment getChildComment(int no, String id) {
		User user = User.builder().id(id).build();
		StoreBoard board = StoreBoard.builder().no(no).build();
		SBoardComment comment = SBoardComment.builder().user(user).board(board).build();
		return storeBoardDao.getChildComment(comment);
	}
	
	public void greatCommentDelete (int no) {
		storeBoardDao.deleteGreatComment(no);
	}
	
	public void childCommentDelete (int no) {
		storeBoardDao.deleteChildsComment(no);
	}
	
	public int getTotalChildCount (int no) {
		return storeBoardDao.getTotalCommentCount(no);
	}
	
	// 게시물 CRUD
	public void addSboard(AddSboardForm form, User user) {
		try {
			BoardCategory category = BoardCategory.builder().no(form.getCatNo()).build();
			BoardProduct product = BoardProduct.builder().no(form.getProductNo()).build();
			StoreBoard board = StoreBoard.builder()
								.user(user)
								.category(category)
								.product(product)
								.name(form.getName())
								.content(form.getContent())
								.build();
			storeBoardDao.insertSboard(board);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateSBoard(AddSboardForm form, int no) {
		try {
			BoardCategory category = BoardCategory.builder().no(form.getCatNo()).build();
			BoardProduct product = BoardProduct.builder().no(form.getProductNo()).build();
			
			StoreBoard board = storeBoardDao.getSBoardByNo(no);
				board.setContent(form.getContent());
				board.setName(form.getName());
				board.setCategory(category);
				board.setProduct(product);
			
			storeBoardDao.updateSBoardByNo(board);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteBoard(int no) {
		StoreBoard board = storeBoardDao.getSBoardByNo(no);
		board.setDeleted("Y");
		
		storeBoardDao.updateSBoardByNo(board);
	}
	
	// 신고관련
	public void insertReport (ReportForm form, User user) {
		
		try {
			StoreBoard board = storeBoardDao.getSBoardByNo(form.getBoardNo());
			ReportReason reason = ReportReason.builder().no(form.getReasonNo()).build();
			SboardReport report = SboardReport.builder()
								.reasonContent(form.getReasonContent())
								.board(board)
								.user(user)
								.reason(reason)
								.build();
			storeBoardDao.insertSboardReport(report);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateReportCount (int no, int reportCount) {
		StoreBoard board = storeBoardDao.getSBoardByNo(no);
		board.setReportCount(reportCount);
		storeBoardDao.updateSBoardByNo(board);
	}
	
	public void updateReport (int no, String report) {
		StoreBoard board= storeBoardDao.getSBoardByNo(no);
		board.setReport(report);
		storeBoardDao.updateSBoardByNo(board);
	}
	
	public List<SboardReport> getReportById (String id){
		return storeBoardDao.getSboardReportById(id);
	}
	
}
