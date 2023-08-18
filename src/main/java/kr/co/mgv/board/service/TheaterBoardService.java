package kr.co.mgv.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.mgv.board.BoardPagination;
import kr.co.mgv.board.form.AddTboardForm;
import kr.co.mgv.board.form.ReportForm;
import kr.co.mgv.board.list.TheaterBoardList;
import kr.co.mgv.board.mapper.TheaterBoardDao;
import kr.co.mgv.board.vo.BoardLocation;
import kr.co.mgv.board.vo.BoardTheater;
import kr.co.mgv.board.vo.ReportReason;
import kr.co.mgv.board.vo.TBoardComment;
import kr.co.mgv.board.vo.TBoardLike;
import kr.co.mgv.board.vo.TboardReport;
import kr.co.mgv.board.vo.TheaterBoard;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TheaterBoardService {
	
	private final TheaterBoardDao theaterBoardDao;
	
	public TheaterBoardList getTBoards(Map<String, Object> param) {
		
		// pagination
		int totalRows = theaterBoardDao.getTotalRows(param);
		
		int page = (int) param.get("page");
		int rows = (int) param.get("rows");
		BoardPagination pagination = new BoardPagination(rows, page, totalRows);
		int begin = pagination.getBegin();
		int end = pagination.getEnd();
		
		param.put("begin", begin);
		param.put("end", end);
		
		// 게시판 목록
		List<TheaterBoard> theaterBoards = theaterBoardDao.getTBoards(param);
		
		// 조회한 목록을 담을 list를 생성한다.
		TheaterBoardList result = new TheaterBoardList();
		
		// 멀티셀렉트박스 관련 (지역/극장) 
		if (param.containsKey("locationNo")) {
			int locationNo = (int) param.get("locationNo");
		}
		if (param.containsKey("theaterNo")) {
			int theaterNo = (int) param.get("theaterNo");			
			
		}
		
		// TheaterBoardList에 pagination과 게시판 목록을 담는다.
		List<BoardLocation> locations = theaterBoardDao.getLocaions();
		result.setPagination(pagination);
		result.setTheaterBoards(theaterBoards);
		result.setLocations(locations);
		
		return result;
		
	}
	
	public List<BoardLocation> getLocations (){
		return theaterBoardDao.getLocaions();
	}
	
	public List<BoardTheater> getTheatersByLocationNo(int locationNo){
		List<BoardTheater> theaters = theaterBoardDao.getTheatersByLocationNo(locationNo);
		return theaters;
	}
	
	// 상세페이지
	public TheaterBoard getTheaterBoardByNo(int no) {
		return theaterBoardDao.getTBoardByNo(no);
	}
	
	public void increseRead(int no) {
		TheaterBoard theaterBoard = theaterBoardDao.getTBoardByNo(no);
		theaterBoard.setReadCount(theaterBoard.getReadCount() + 1);
		theaterBoardDao.updateTBoardByNo(theaterBoard);
	}
	
	public void updateBoardLike(int no, int likeCount) {
		TheaterBoard theaterBoard = theaterBoardDao.getTBoardByNo(no);
		theaterBoard.setLikeCount(likeCount);
		theaterBoardDao.updateTBoardByNo(theaterBoard);
	}
	
	public void insertBoardLike (TBoardLike like) {
		theaterBoardDao.insertTBoardLike(like);
	}
	
	public TBoardLike getLike(TBoardLike like) {
		return theaterBoardDao.getLikeByBnoAndId(like);
	}
	
	public void updateTboardLike(TBoardLike like) {
		theaterBoardDao.updateLike(like);
	}
	
	// 댓글관련 - updateBoardComment
	public void updateBoardComment(int no, int commentCount) {
		TheaterBoard board = theaterBoardDao.getTBoardByNo(no);
		board.setCommentCount(commentCount);
		
		theaterBoardDao.updateTBoardByNo(board);
	}
	
	public void TBoardCommentInsert(TBoardComment comment) {
		theaterBoardDao.insertTBoardComment(comment);
	}
	
	public List<TBoardComment> getComments(int no) {
		return theaterBoardDao.geTBoardComments(no);
	}
	
	public List<TBoardComment> getChildComments(int no) {
		return theaterBoardDao.getTBoardChildComments(no);
	}
	
	public TBoardComment getGreatComment(int no, String id) {
		User user = User.builder().id(id).build();
		TheaterBoard board = TheaterBoard.builder().no(no).build();
		TBoardComment comment = TBoardComment.builder().user(user).board(board).build();
		return theaterBoardDao.getGreatComment(comment);
	}
	
	public TBoardComment getChildComment(int no, String id) {
		User user = User.builder().id(id).build();
		TheaterBoard board = TheaterBoard.builder().no(no).build();
		TBoardComment comment = TBoardComment.builder().user(user).board(board).build();
		return theaterBoardDao.getChildComment(comment);
	}
	
	public void greatCommentDelete (int no) {
		theaterBoardDao.deleteGreatComment(no);
	}
	
	public void childCommentsDelete (int no) {
		theaterBoardDao.deleteChildsComment(no);
	}
	
	public int getTotalChildCount (int no) {
		return theaterBoardDao.getTotalCommentCount(no);
	}
	
	// 게시물 CRUD 관련
	public void addTboard(AddTboardForm form, User user) {
		try {
			BoardTheater theater = BoardTheater.builder().no(form.getTheaterNo()).build();
			BoardLocation location = BoardLocation.builder().no(form.getLocationNo()).build();
			TheaterBoard board = TheaterBoard.builder()
								.user(user)
								.theater(theater)
								.location(location)
								.name(form.getName())
								.content(form.getContent())
								.build();
			theaterBoardDao.insertTboard(board);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateTBoard(AddTboardForm form, int no) {
	    try {
	    	BoardTheater theater = BoardTheater.builder().no(form.getTheaterNo()).build();
			BoardLocation location = BoardLocation.builder().no(form.getLocationNo()).build();
			TheaterBoard board = TheaterBoard.builder()
								.no(no)
								.theater(theater)
								.location(location)
								.name(form.getName())
								.content(form.getContent())
								.build();

	        theaterBoardDao.updateTBoardByNo(board);         
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void deleteBoard(int no) {
		TheaterBoard board = theaterBoardDao.getTBoardByNo(no);
		board.setDeleted("Y");
		
		theaterBoardDao.updateTBoardByNo(board);
	}
	
	// 신고관련
	public void insertReport (ReportForm form, User user) {
		
		try {
			TheaterBoard board = theaterBoardDao.getTBoardByNo(form.getBoardNo());
			ReportReason reason = ReportReason.builder().no(form.getReasonNo()).build();
			TboardReport report = TboardReport.builder()
								.reasonContent(form.getReasonContent())
								.board(board)
								.user(user)
								.reason(reason)
								.build();
			theaterBoardDao.insertTboardReport(report);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateReportCount (int no, int reportCount) {
		TheaterBoard board = theaterBoardDao.getTBoardByNo(no);
		board.setReportCount(reportCount);
		theaterBoardDao.updateTBoardByNo(board);
	}
	
	public void updateReport (int no, String report) {
		TheaterBoard board= theaterBoardDao.getTBoardByNo(no);
		board.setReport(report);
		theaterBoardDao.updateTBoardByNo(board);
	}
	
	public List<TboardReport> getReportById (String id){
		return theaterBoardDao.getTboardReportById(id);
	}
	
	
	
	
}
