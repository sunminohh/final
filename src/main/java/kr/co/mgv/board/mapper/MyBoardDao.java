package kr.co.mgv.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.board.list.BoardList;

@Mapper
public interface MyBoardDao {

	List<BoardList> getBoardList(Map<String, Object> param);
	int getListTotalRows (Map<String, Object> param);

	List<BoardList> getBoardListByComment(Map<String, Object> param);
	int getListTotalRowsByComment (Map<String, Object> param);

	List<BoardList> getBoardListBylike(Map<String, Object> param);
	int getListTotalRowsBylike (Map<String, Object> param);

	List<BoardList> getPartyByJoin(Map<String, Object> param);
	int getTotalRowsByJoin (Map<String, Object> param);
}
