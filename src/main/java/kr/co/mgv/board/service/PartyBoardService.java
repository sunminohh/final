package kr.co.mgv.board.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.mgv.board.BoardPagination;
import kr.co.mgv.board.form.AddBoardNoticeForm;
import kr.co.mgv.board.form.AddPboardForm;
import kr.co.mgv.board.form.ReportForm;
import kr.co.mgv.board.list.PartyBoardList;
import kr.co.mgv.board.mapper.BoardNoticeDao;
import kr.co.mgv.board.mapper.PartyBoardDao;
import kr.co.mgv.board.mapper.TheaterBoardDao;
import kr.co.mgv.board.vo.BoardLocation;
import kr.co.mgv.board.vo.MovieBoard;
import kr.co.mgv.board.vo.PBoardComment;
import kr.co.mgv.board.vo.PBoardReport;
import kr.co.mgv.board.vo.PartyBoard;
import kr.co.mgv.board.vo.PartyBoardSchedule;
import kr.co.mgv.board.vo.PartyJoin;
import kr.co.mgv.board.vo.ReportReason;
import kr.co.mgv.board.vo.SboardReport;
import kr.co.mgv.board.vo.StoreBoard;
import kr.co.mgv.board.websocket.handler.NoticeWebsocketHandler;
import kr.co.mgv.schedule.vo.Schedule;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartyBoardService {

	private final PartyBoardDao partyBoardDao;
	private final TheaterBoardDao theaterBoardDao;
	private final NoticeWebsocketHandler noticeWebsocketHandler;
	private final BoardNoticeDao boardNoticeDao;
	
	// 등록폼 관련
	public List<PartyBoardSchedule> getScheduleList(Map<String, Object> param) {
		return partyBoardDao.getSceduleByDateAndTNoAndMNo(param);
	} 
	
	public PartyBoardSchedule getSceduleById(int id) {
		return partyBoardDao.getSceduleById(id);
	}
	
	// CRUD
	public void insertPBoard (AddPboardForm form, User user) {
		
		PartyBoardSchedule schedule = PartyBoardSchedule.builder()
									  .id(form.getScheduleId()).build();
		PartyBoard board = PartyBoard.builder()
						   .user(user)
						   .name(form.getName())
						   .content(form.getContent())
						   .headCount(form.getHeadCount())
						   .gender(form.getGender())
						   .schedule(schedule)
						   .build();
		partyBoardDao.insertPboard(board);
	}
	
	public void updatePBoard (int no, AddPboardForm form) {
		
		PartyBoardSchedule schedule = PartyBoardSchedule.builder()
				  .id(form.getScheduleId()).build();
		PartyBoard board = partyBoardDao.getPBoardByNo(no);
		board.setName(form.getName());
		board.setContent(form.getContent());
		board.setHeadCount(form.getHeadCount());
		board.setGender(form.getGender());
		board.setSchedule(schedule);
		
		partyBoardDao.updatePBoardByNo(board);
	}
	
	public void deletePBoard (int no, String deleted) {
	
		PartyBoard board = partyBoardDao.getPBoardByNo(no);
		board.setDeleted(deleted);
		partyBoardDao.updatePBoardByNo(board);
	}
	
	// 목록 관련
	public PartyBoardList getPBoards(Map<String, Object> param) {
		// pagination
		int totalRows = partyBoardDao.getTotalRows(param);
		int page = (int) param.get("page");
		int rows = (int) param.get("rows");
		BoardPagination pagination = new BoardPagination(rows, page, totalRows);
		int begin = pagination.getBegin();
		int end= pagination.getEnd();
		
		param.put("begin", begin);
		param.put("end", end);
		
		
		// 게시판 목록
		List<PartyBoard> partyBoards = partyBoardDao.getPartyBoards(param);
		
		// 조회한 목록을 담을 list 생성
		PartyBoardList result = new PartyBoardList();
		if(param.containsKey("locationNo")) {
			int locationNo = (int) param.get("locationNo");
		}
		if (param.containsKey("theaterNo")) {
			int theaterNo = (int) param.get("theaterNo");			
		}
		
		List<BoardLocation> locations = theaterBoardDao.getLocaions();
		result.setPagination(pagination);
		result.setLocations(locations);
		result.setParytBoards(partyBoards);
		
		
		return result;
	}
	
	// 상세 페이지 관련
	public PartyBoard getPBoardByNo (int no) {
		return partyBoardDao.getPBoardByNo(no);
	}
	
	public void increaseReadCount(int no) {
		PartyBoard board = partyBoardDao.getPBoardByNo(no);
		board.setReadCount(board.getReadCount() + 1);
		partyBoardDao.updatePBoardByNo(board);
	}
	
	// 신청관련
	public void insertPartyJoin (int no, User user) {
		PartyBoard board = PartyBoard.builder().no(no).build();
		PartyJoin join = PartyJoin.builder().board(board).user(user).build();
		partyBoardDao.insertPartyJoin(join);
	}
	
	public PartyJoin getJoinByPnoAndId(int no, User user) {
		PartyBoard board = PartyBoard.builder().no(no).build();
		PartyJoin join = PartyJoin.builder().board(board).user(user).build();
		return partyBoardDao.getJoinByPnoAndId(join);
	}
	
	public List<PartyJoin> getJoinByPnoAndAccept (int no, String accept){
		PartyBoard board = PartyBoard.builder().no(no).build();
		PartyJoin join = PartyJoin.builder().board(board).accept(accept).build();
		return partyBoardDao.getJoinByPnoAndAccept(join);
	}
	
	public void updateJoin (int no, User user, String request, String writerId) throws IOException {
		PartyBoard board = partyBoardDao.getPBoardByNo(no);
		PartyJoin join = PartyJoin.builder().board(board).user(user).request(request).build();
		PartyJoin savedJoin = partyBoardDao.getJoinByPnoAndId(join);
		
		String fromId = join.getUser().getId();
		String type = "파티";
		int boardNo = join.getBoard().getNo();
		String BoardName = board.getName();
		if (BoardName.length() > 8) {
			BoardName =  BoardName.substring(0, 8);
		}
		
		if(savedJoin != null && "N".equals(savedJoin.getRequest()) && !writerId.equals(fromId)) {
    		String text = "["+ type + "]게시판 [" +BoardName+ "...]에 " + fromId + "님이 파티를 신청 했습니다."+boardNo; 
			noticeWebsocketHandler.sendMessage(writerId, text);
			log.info("text -> {}",text);
			AddBoardNoticeForm form = AddBoardNoticeForm.builder()
					  .boardType(type)
					  .boardNo(boardNo)
					  .fromId(fromId)
					  .toId(writerId)
					  .code("join")
					  .boardName(BoardName)
					  .build();
			boardNoticeDao.insertNotice(form);
    	} 
    	
    	if(savedJoin == null && !writerId.equals(fromId)) {
    		String text = "["+ type + "]게시판 [" +BoardName+ "...]에 " + fromId + "님이 파티를 신청 했습니다."+boardNo; 
			noticeWebsocketHandler.sendMessage(writerId, text);
			log.info("text -> {}",text);
			AddBoardNoticeForm form = AddBoardNoticeForm.builder()
					  .boardType(type)
					  .boardNo(boardNo)
					  .fromId(fromId)
					  .toId(writerId)
					  .code("join")
					  .boardName(BoardName)
					  .build();
			boardNoticeDao.insertNotice(form);
    	}
		
		
		partyBoardDao.updateJoin(join);
	}
	
	public void updateRequestCount (PartyBoard board) {
		partyBoardDao.updatePBoardByNo(board);
	}
	
	public void acceptJoin (int no, String id, String accept) {
		PartyBoard board = PartyBoard.builder().no(no).build();
		User user = User.builder().id(id).build();
		PartyJoin join = PartyJoin.builder().board(board).user(user).accept(accept).build();
		partyBoardDao.updateJoin(join);
	}

	
	public void updateAcceptCount (int no, int AcceptCount) {
		PartyBoard board = partyBoardDao.getPBoardByNo(no);
		board.setAcceptCount(AcceptCount);
		partyBoardDao.updatePBoardByNo(board);
	}
	
	public int getAcceptCount (int no) {
		return partyBoardDao.getAcceptCount(no);
	}
	
	public void partyComplete (int no) {
		PartyBoard board = partyBoardDao.getPBoardByNo(no);
		board.setComplete("Y");
		partyBoardDao.updatePBoardByNo(board);
	}
	
	// 신고관련
	public void insertReport (ReportForm form, User user) {
		
		try {
			PartyBoard board = partyBoardDao.getPBoardByNo(form.getBoardNo());
			ReportReason reason = ReportReason.builder().no(form.getReasonNo()).build();
			
			PBoardReport report = PBoardReport
								  .builder()
								  .user(user)
								  .reasonContent(form.getReasonContent())
								  .board(board)
								  .reason(reason).build();
			partyBoardDao.insertPboardReport(report);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateReport (int no, String report) {
		PartyBoard board = partyBoardDao.getPBoardByNo(no);
		board.setReport(report);
		partyBoardDao.updatePBoardByNo(board);
	}

	public void updateReportCount (int no, int reportCount) {
		PartyBoard board = partyBoardDao.getPBoardByNo(no);
		board.setReportCount(reportCount);
		partyBoardDao.updatePBoardByNo(board);
	}
	
	public List<PBoardReport> getPBoardReportById (String id) {
		return partyBoardDao.getPboardReportById(id);
	}
	
	// 댓글 관련
	public void insertComment(PBoardComment comment, String writerId) throws IOException {
		partyBoardDao.insertPBoardComment(comment);
		
		String type = "파티";
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
									  .boardName(boardName)
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
						  .boardName(boardName)
						  .build();
				boardNoticeDao.insertNotice(form);
		}
		
		// 내 게시글의 다른 사용자의 댓글에 내가 아닌 사용자가 대댓글을 달았다
		if(comment.getGreat() != null &&  !comment.getGreat().getUser().getId().equals(fromId) && !writerId.equals(fromId) ) {
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
					  .boardName(boardName)
					  .build();
			boardNoticeDao.insertNotice(form);
		}
	}
	
	public List<PBoardComment> getGreatComments(int no){
		return partyBoardDao.getPBoardComments(no);
	}
	
	public List<PBoardComment> getchildComments(int no){
		return partyBoardDao.getPBoardChildComments(no);
	}
	
	public void updateBoardComment(int no, int commentCount) {
		PartyBoard board = partyBoardDao.getPBoardByNo(no);
		board.setCommentCount(commentCount);
		partyBoardDao.updatePBoardByNo(board);
	}
	
	public int getTotalChildCount(int boardNo) {
		return partyBoardDao.getTotalChildCount(boardNo);
	}
	
	public void deleteComment(int commentNo) {
		partyBoardDao.deleteComment(commentNo);
	}
	
	public void deleteChildComments (int greatNo) {
		partyBoardDao.deleteChildComments(greatNo);
	}
}
