package kr.co.mgv.board.tboard;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TheaterBoardDao {

	// 게시물 목록 관련
	int getTotalRows(Map<String, Object> param);
	List<TheaterBoard> getTBoards(Map<String, Object> param);
	// 게시물 목록 멀티 셀렉트 관련
	List<BoardTheater> getTheatersByLocationNo(int locationNo);
	List<BoardLocation> getLocaions();
	
	// 게시물 상세페이지 관련
	TheaterBoard getTBoardByNo (int tbNo);
	
	
	
	
}
