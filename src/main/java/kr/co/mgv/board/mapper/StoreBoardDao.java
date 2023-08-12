package kr.co.mgv.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.board.vo.BoardCategory;
import kr.co.mgv.board.vo.BoardProduct;
import kr.co.mgv.board.vo.StoreBoard;


@Mapper
public interface StoreBoardDao {

	// 게시물 목록 관련
	int getTotalRows(Map<String, Object> param);
	List<StoreBoard> getSBoards(Map<String, Object> param);
	// 게시물 목록 멀티 셀렉트 관련
	List<BoardProduct> getProductsByCatNo (int CatNo);
	List<BoardCategory> getCatetories();
	
	// 게시물 상세페이지 관련
	StoreBoard getSBoardByNo (int sbNo);
	
}
