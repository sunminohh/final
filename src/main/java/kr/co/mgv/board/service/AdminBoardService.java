package kr.co.mgv.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.mgv.board.BoardPagination;
import kr.co.mgv.board.list.BoardList;
import kr.co.mgv.board.list.MyBoardList;
import kr.co.mgv.board.mapper.AdminBoardDao;
import kr.co.mgv.board.vo.BoardReport;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminBoardService {

	private final AdminBoardDao adminBoardDao;
	
	public MyBoardList getBoardList(Map<String, Object> param) {
		
		int totalRows = adminBoardDao.getTotalRows(param);
		int page = (int) param.get("page");
		BoardPagination pagination = new BoardPagination(page, totalRows);
		int begin = pagination.getBegin()-1;
		int end = pagination.getEnd();
		param.put("begin", begin);
		param.put("end", end);
		
		List<BoardList> boards = adminBoardDao.getBoardListByReport(param);
		
		MyBoardList result = new MyBoardList();
		result.setList(boards);
		result.setPagination(pagination);
		
		return result;
	}
	
	public BoardList getBoardDetail(Map<String, Object> param) {
		return adminBoardDao.getBoardDetail(param);
	}
	
	public int getTotalrows() {
		Map<String , Object> param = new HashMap<String, Object>();
		param.put("boards", "all");
		return adminBoardDao.getTotalRows(param);
	}
	
	public List<BoardReport> getReports (Map<String, Object> param){
		return adminBoardDao.getReportReasons(param);
	}
	
	public void deleteReportBoard (Map<String, Object> param) {
		adminBoardDao.updateReportBoard(param);
	}

	public void resotreReportBoard (Map<String, Object> param) {
		adminBoardDao.updateReportBoard(param);
	}
	
	public void deleteReportReasonByNo (Map<String, Object> param) {
		adminBoardDao.deleteReportReasonByNo(param);
	}
}
