package kr.co.mgv.board.tboard;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.mgv.board.BoardPagination;
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
		
		// TheaterBoardList에 pagination과 게시판 목록을 담는다.
		TheaterBoardList result = new TheaterBoardList();
		result.setPagination(pagination);
		result.setTheaterBoards(theaterBoards);
		
		return result;
		
	}
	
}
