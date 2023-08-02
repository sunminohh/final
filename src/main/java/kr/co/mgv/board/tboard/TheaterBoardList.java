package kr.co.mgv.board.tboard;

import java.util.List;
import java.util.Map;

import kr.co.mgv.board.BoardPagination;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TheaterBoardList {

	private BoardPagination pagination;
	private List<TheaterBoard> theaterBoards;
}
