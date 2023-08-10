package kr.co.mgv.board.list;

import java.util.List;

import kr.co.mgv.board.BoardPagination;
import kr.co.mgv.board.vo.MovieBoard;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieBoardList {

	private BoardPagination pagination;
	private List<MovieBoard> movieBoards;
}
