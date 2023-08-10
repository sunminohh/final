package kr.co.mgv.board.list;
import java.util.List;
import kr.co.mgv.board.BoardPagination;
import kr.co.mgv.board.vo.BoardCategory;
import kr.co.mgv.board.vo.StoreBoard;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreBoardList {

	private BoardPagination pagination;
	private List<StoreBoard> storeBoards;
	private List<BoardCategory> categories;
}
