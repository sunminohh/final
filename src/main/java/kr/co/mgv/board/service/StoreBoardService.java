package kr.co.mgv.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.mgv.board.BoardPagination;
import kr.co.mgv.board.list.StoreBoardList;
import kr.co.mgv.board.mapper.StoreBoardDao;
import kr.co.mgv.board.vo.BoardCategory;
import kr.co.mgv.board.vo.BoardProduct;
import kr.co.mgv.board.vo.StoreBoard;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreBoardService {

	private final StoreBoardDao storeBoardDao;
	
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
	
	public List<BoardProduct> getProductsByCatNo(int catNo){
		List<BoardProduct> products = storeBoardDao.getProductsByCatNo(catNo);
		return products;
	}
}
