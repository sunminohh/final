package kr.co.mgv.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.board.vo.PartyBoard;
import kr.co.mgv.board.vo.PartyBoardSchedule;

@Mapper
public interface PartyBoardDao {

	// 등록폼 관련
	List<PartyBoardSchedule> getSceduleByDateAndTNoAndMNo(Map<String, Object> param);
	PartyBoardSchedule getSceduleById (int id);
	
	// CRUD
	void insertPboard(PartyBoard board);
	
	// 목록 관련
	int getTotalRows(Map<String, Object> param);
	List<PartyBoard> getPartyBoards(Map<String, Object> param);
}
