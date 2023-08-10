package kr.co.mgv.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.board.vo.MBoardComment;
import kr.co.mgv.board.vo.MBoardLike;
import kr.co.mgv.board.vo.MovieBoard;

@Mapper
public interface MovieBoardDao {

	int getTotalRows(Map<String, Object> param);
	List<MovieBoard> getMBoards(Map<String, Object> param);
	MovieBoard getMBoardByNo (int mbNo);
	void updateMBoardByNo(MovieBoard movieBoard );
	void insertMBoardLike(MBoardLike like);
	MBoardLike getLikeByBnoAndId (MBoardLike like);
	void updateLike(MBoardLike like);
	
	void insertMBoardComment(MBoardComment comment);
	List<MBoardComment> getMBoardComments(int no);
	List<MBoardComment> getMBoardChildComments(int no);
	
}
