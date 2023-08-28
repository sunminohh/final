package kr.co.mgv.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.board.vo.PBoardReport;
import kr.co.mgv.board.vo.PartyBoard;
import kr.co.mgv.board.vo.PartyBoardSchedule;
import kr.co.mgv.board.vo.PartyJoin;

@Mapper
public interface PartyBoardDao {

	// 등록폼 관련
	List<PartyBoardSchedule> getSceduleByDateAndTNoAndMNo(Map<String, Object> param);
	PartyBoardSchedule getSceduleById (int id);
	
	// CRUD
	void insertPboard(PartyBoard board);
	void updatePBoardByNo(PartyBoard board);
	
	// 목록 관련
	int getTotalRows(Map<String, Object> param);
	List<PartyBoard> getPartyBoards(Map<String, Object> param);
	
	// 상세페이지 관련
	PartyBoard getPBoardByNo(int no);
	
	// 파티 신청 관련
	void insertPartyJoin(PartyJoin join);
	PartyJoin getJoinByPnoAndId(PartyJoin join);
	List<PartyJoin> getJoinByPnoAndAccept(PartyJoin join);
	void updateJoin (PartyJoin join);
	int getAcceptCount (int no);
	
	// 신고관련
	void insertPboardReport(PBoardReport report);
	List<PBoardReport> getPboardReportById(String id);
}
