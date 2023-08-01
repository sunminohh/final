package kr.co.mgv.board;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TboardMapper {

	int getTotalRows(Map<String, Object> param);
	List<TheaterBoard> getTBoards(Map<String, Object> param);
}
