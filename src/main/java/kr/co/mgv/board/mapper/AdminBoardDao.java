package kr.co.mgv.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.board.list.BoardList;

@Mapper
public interface AdminBoardDao {

	List<BoardList> getBoardListByReport(Map<String, Object> param);
	int getTotalRows(Map<String, Object> param);
}
