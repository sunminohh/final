package kr.co.mgv.board.service;

import java.util.HashMap;
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
	
	public String getProfileImg(String id) {
		return myBoardDao.getUserProfileImg(id);
	}
	
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

	public  MyBoardList getBoardListByComment(Map<String, Object> param) {
		
		int totalRows = myBoardDao.getListTotalRowsByComment(param);
		int page = (int) param.get("page");
		BoardPagination pagination = new BoardPagination(page, totalRows);
		int begin = pagination.getBegin()-1;
		int end = pagination.getEnd();
		param.put("begin", begin);
		param.put("end", end);
		
		List<BoardList> boards = myBoardDao.getBoardListByComment(param);
		
		MyBoardList result = new MyBoardList();
		
		result.setList(boards);
		result.setPagination(pagination);
		
		return result;
	}

	public  MyBoardList getBoardListByLike(Map<String, Object> param) {
		
		int totalRows = myBoardDao.getListTotalRowsBylike(param);
		int page = (int) param.get("page");
		BoardPagination pagination = new BoardPagination(page, totalRows);
		int begin = pagination.getBegin()-1;
		int end = pagination.getEnd();
		param.put("begin", begin);
		param.put("end", end);
		
		List<BoardList> boards = myBoardDao.getBoardListBylike(param);
		
		MyBoardList result = new MyBoardList();
		
		result.setList(boards);
		result.setPagination(pagination);
		
		return result;
	}

	public  MyBoardList getBoardListByJoin(Map<String, Object> param) {
		
		int totalRows = myBoardDao.getTotalRowsByJoin(param);
		int page = (int) param.get("page");
		BoardPagination pagination = new BoardPagination(page, totalRows);
		int begin = pagination.getBegin()-1;
		int end = pagination.getEnd();
		param.put("begin", begin);
		param.put("end", end);
		
		List<BoardList> boards = myBoardDao.getPartyByJoin(param);
		
		MyBoardList result = new MyBoardList();
		
		result.setList(boards);
		result.setPagination(pagination);
		
		return result;
	}
	
	public int getBoardsTotalRows(String id) {
		Map<String , Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("boards", "all");
		
		return myBoardDao.getListTotalRows(param);
	}
	
	public int getCommentTotalRows(String id) {
		Map<String , Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("boards", "all");
		
		return myBoardDao.getListTotalRowsByComment(param);
	}
	public int getlikeTotalRows(String id) {
		Map<String , Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("boards", "all");
		
		return myBoardDao.getListTotalRowsBylike(param);
	}
	
	public List<BoardList> getBest5(String sort){
		Map<String , Object> param = new HashMap<String, Object>();
		param.put("sort", sort);
		return myBoardDao.getBest5(param);
	}
	
}
