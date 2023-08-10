package kr.co.mgv.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.mgv.board.BoardPagination;
import kr.co.mgv.board.list.TheaterBoardList;
import kr.co.mgv.board.mapper.TheaterBoardDao;
import kr.co.mgv.board.vo.BoardLocation;
import kr.co.mgv.board.vo.BoardTheater;
import kr.co.mgv.board.vo.TheaterBoard;
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
	
	public List<BoardTheater> getTheatersByLocationNo(int locationNo){
		List<BoardTheater> theaters = theaterBoardDao.getTheatersByLocationNo(locationNo);
		return theaters;
	}
	
}