package kr.co.mgv.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.board.vo.PartyBoardSchedule;

@Mapper
public interface PartyBoardDao {

	List<PartyBoardSchedule> getSceduleByDateAndTNoAndMNo(Map<String, Object> param);
	PartyBoardSchedule getSceduleById (int id);
}
