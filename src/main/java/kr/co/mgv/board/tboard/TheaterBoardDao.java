package kr.co.mgv.board.tboard;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TheaterBoardDao {

	int getTotalRows(Map<String, Object> param);
	List<TheaterBoard> getTBoards(Map<String, Object> param);
}
