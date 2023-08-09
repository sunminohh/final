package kr.co.mgv.board.mboard;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MovieBoardDao {

	int getTotalRows(Map<String, Object> param);
	List<MovieBoard> getMBoards(Map<String, Object> param);
	MovieBoard getMBoardByNo (int mbNo);
	void updateMBoardByNo(MovieBoard movieBoard );
	
	void insertMBoardComment(MBoardComment comment);
	
}
