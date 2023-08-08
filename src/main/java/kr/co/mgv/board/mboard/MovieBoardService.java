package kr.co.mgv.board.mboard;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import kr.co.mgv.board.BoardPagination;
import kr.co.mgv.movie.vo.Movie;
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
	
	public void increaseRead(int no) {
		MovieBoard movieBoard = movieBoardDao.getMBoardByNo(no);
		movieBoard.setReadCount(movieBoard.getReadCount() + 1);
		movieBoardDao.updateMBoardByNo(movieBoard);

	}
	
	public void updateMBoardByNo (MBoardForm form) {
		MovieBoard movieBoard = movieBoardDao.getMBoardByNo(form.getNo());
		BeanUtils.copyProperties(form, movieBoard);
		
		movieBoardDao.updateMBoardByNo(movieBoard);
	}
	
	public MovieBoard getMovieBoardByNo(int no) {
		MovieBoard movieBoard = movieBoardDao.getMBoardByNo(no);
		
		return movieBoard;
	}
	

}
