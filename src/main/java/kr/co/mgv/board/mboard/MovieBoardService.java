package kr.co.mgv.board.mboard;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.mgv.board.BoardPagination;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieBoardService {
	
	private final MovieBoardDao movieBoardDao;
	
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
	

}
