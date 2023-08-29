package kr.co.mgv.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.mgv.board.BoardPagination;
import kr.co.mgv.board.list.BoardList;
import kr.co.mgv.board.list.MyBoardList;
import kr.co.mgv.board.mapper.MyBoardDao;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyBoardService {

	private final MyBoardDao myBoardDao;
	
	public  MyBoardList getBoardList(Map<String, Object> param) {
		
		int totalRows = myBoardDao.getListTotalRows(param);
		int page = (int) param.get("page");
		BoardPagination pagination = new BoardPagination(page, totalRows);
		int begin = pagination.getBegin()-1;
		int end = pagination.getEnd();
		param.put("begin", begin);
		param.put("end", end);
		
		List<BoardList> boards = myBoardDao.getBoardList(param);
		
		MyBoardList result = new MyBoardList();
		
		result.setList(boards);
		result.setPagination(pagination);
		
		return result;
	}
}
