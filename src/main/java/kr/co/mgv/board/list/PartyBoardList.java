package kr.co.mgv.board.list;

import java.util.List;

import kr.co.mgv.board.BoardPagination;
import kr.co.mgv.board.vo.BoardLocation;
import kr.co.mgv.board.vo.PartyBoard;
import kr.co.mgv.board.vo.TheaterBoard;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyBoardList {

	private BoardPagination pagination;
	private List<PartyBoard> parytBoards;
	private List<BoardLocation> locations;
}
