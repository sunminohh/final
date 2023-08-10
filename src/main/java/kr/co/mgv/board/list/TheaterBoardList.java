package kr.co.mgv.board.list;

import java.util.List;

import kr.co.mgv.board.BoardPagination;
import kr.co.mgv.board.vo.BoardLocation;
import kr.co.mgv.board.vo.TheaterBoard;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TheaterBoardList {

	private BoardPagination pagination;
	private List<TheaterBoard> theaterBoards;
	private List<BoardLocation> locations;
}
