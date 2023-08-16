package kr.co.mgv.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.board.list.MboardCommentList;
import kr.co.mgv.board.vo.MBoardComment;
import kr.co.mgv.board.vo.MBoardLike;
import kr.co.mgv.board.vo.MboardReport;
import kr.co.mgv.board.vo.MovieBoard;
import kr.co.mgv.board.vo.ReportReason;
import kr.co.mgv.movie.vo.Movie;

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
	MBoardComment getGreatComment(MBoardComment comment);
	MBoardComment getChildComment(MBoardComment comment);
	int getTotalCommentCount (int no);
	void deleteGreatComment(int no);
	void deleteChildsComment(int no);
	
	List<Movie> getMovieTitle();
	void insertMBoard(MovieBoard board);
	
	List<ReportReason> getReportReason();
	void insertMboardReport (MboardReport report);
	List<MboardReport> getMboardReportById (String id);
}
