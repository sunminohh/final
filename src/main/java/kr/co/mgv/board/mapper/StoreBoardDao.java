package kr.co.mgv.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.mgv.board.vo.BoardCategory;
import kr.co.mgv.board.vo.BoardProduct;
import kr.co.mgv.board.vo.SBoardComment;
import kr.co.mgv.board.vo.SBoardLike;
import kr.co.mgv.board.vo.StoreBoard;


@Mapper
public interface StoreBoardDao {

	// 게시물 목록 관련
	int getTotalRows(Map<String, Object> param);
	List<StoreBoard> getSBoards(Map<String, Object> param);
	// 게시물 목록 멀티 셀렉트 관련
	List<BoardProduct> getProductsByCatNo (int CatNo);
	List<BoardCategory> getCatetories();
	
	// 게시물 상세페이지 관련
	StoreBoard getSBoardByNo (int sbNo);
	void updateSBoardByNo(StoreBoard board);
	
	// 좋아요 관련
	void insertSBoardLike(SBoardLike like);
	SBoardLike getLikeByBnoAndId (SBoardLike like);
	void updateLike (SBoardLike like);
	
	// 댓글관련
	void insertSBoardComment(SBoardComment comment);
	List<SBoardComment> getSBoardComments(int no);
	List<SBoardComment> getSBoardChildComments(int no);
	SBoardComment getGreatComment(SBoardComment comment);
	SBoardComment getChildComment(SBoardComment comment);
	int getTotalCommentCount (int no);
	void deleteGreatComment(int no);
	void deleteChildsComment(int no);
	
	// 게시글 등록 관련
	void insertSboard(StoreBoard board);
}
