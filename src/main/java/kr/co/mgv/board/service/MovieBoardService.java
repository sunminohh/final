package kr.co.mgv.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import kr.co.mgv.board.BoardPagination;
import kr.co.mgv.board.form.MBoardForm;
import kr.co.mgv.board.list.MovieBoardList;
import kr.co.mgv.board.mapper.MovieBoardDao;
import kr.co.mgv.board.vo.MBoardComment;
import kr.co.mgv.board.vo.MBoardLike;
import kr.co.mgv.board.vo.MovieBoard;
import kr.co.mgv.movie.vo.Movie;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieBoardService {
	
	private final MovieBoardDao movieBoardDao;

// 게시물 리스트
	public MovieBoardList getMBoards(Map<String, Object> param) {
		
		// pagination
		int totalRows = movieBoardDao.getTotalRows(param);
		
		int page = (int) param.get("page");
		int rows = (int) param.get("rows");
		BoardPagination pagination = new BoardPagination(rows, page, totalRows);
		int begin = pagination.getBegin();
		int end = pagination.getEnd();
		
		param.put("begin", begin);
		param.put("end",end);
		
		// 게시판 목록
		List<MovieBoard> movieBoards = movieBoardDao.getMBoards(param);
		
		// MovieBoardList에 pagination과 게시판 목록을 담는다.
		MovieBoardList result = new MovieBoardList();
		result.setPagination(pagination);
		result.setMovieBoards(movieBoards);
		
		return result;
	}

// 상세페이지
	public void increaseRead(int no) {
		MovieBoard movieBoard = movieBoardDao.getMBoardByNo(no);
		movieBoard.setReadCount(movieBoard.getReadCount() + 1);
		movieBoardDao.updateMBoardByNo(movieBoard);

	}
	
	public void updateBoardLike(int no, int likeCount) {
		MovieBoard movieBoard = movieBoardDao.getMBoardByNo(no);
		movieBoard.setLikeCount(likeCount);
		
		movieBoardDao.updateMBoardByNo(movieBoard);
		
	}
	
	public void insertBoardLike(MBoardLike like) {
		movieBoardDao.insertMBoardLike(like);
	}
	
	public MBoardLike getLike(MBoardLike like) {
		return movieBoardDao.getLikeByBnoAndId(like);
	}
	
	public void updateMBoardLike (MBoardLike like) {
		movieBoardDao.updateLike(like);
	}
	
	public void updateBoardComment(int no, int commentCount) {
		MovieBoard movieBoard = movieBoardDao.getMBoardByNo(no);
		movieBoard.setCommentCount(commentCount);
		
		movieBoardDao.updateMBoardByNo(movieBoard);
		
	}
	
	public MovieBoard getMovieBoardByNo(int no) {
		MovieBoard movieBoard = movieBoardDao.getMBoardByNo(no);
		
		return movieBoard;
	}
	
// comment
	
	public void MBoardCommentInsert(MBoardComment comment) {
		movieBoardDao.insertMBoardComment(comment);
	}
	
	public List<MBoardComment> getComments(int no) {
		List<MBoardComment> comments = movieBoardDao.getMBoardComments(no);
		
		return comments;
	}
	
	public List<MBoardComment> getChildComments(int no) {
		List<MBoardComment> childComments = movieBoardDao.getMBoardChildComments(no);
		return childComments;
	}
	
	public MBoardComment getGreatComment(int no, String id) {
		User user = User.builder()
				.id(id)
				.build();
		
		MovieBoard board = MovieBoard.builder()
						.no(no)
						.build();
		MBoardComment comment = MBoardComment.builder()
					.user(user)
					.board(board)
					.build();
		
		return movieBoardDao.getGreatComment(comment);
		
	}
	
	public MBoardComment getChildComment(int no, String id) {
		
		User user = User.builder()
				.id(id)
				.build();
		
		MovieBoard board = MovieBoard.builder()
						.no(no)
						.build();
		MBoardComment comment = MBoardComment.builder()
					.user(user)
					.board(board)
					.build();
		
		return movieBoardDao.getChildComment(comment);
	}

	// 게시물 등록 관련
	public List<Movie> getMovieTitle() {
		return movieBoardDao.getMovieTitle();
	} 
	
	public void addMBoard(MBoardForm form) {
	    try {
	        MovieBoard board = new MovieBoard();
	        BeanUtils.copyProperties(board, form);

	        movieBoardDao.insertMBoard(board);            
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
}
