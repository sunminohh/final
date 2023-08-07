package kr.co.mgv.board.mboard;

import java.util.List;

import kr.co.mgv.board.BoardPagination;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieBoardList {

	private BoardPagination pagination;
	private List<MovieBoard> movieBoards;
}
