package kr.co.mgv.board.sboard;
import java.util.List;
import kr.co.mgv.board.BoardPagination;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreBoardList {

	private BoardPagination pagination;
	private List<StoreBoard> storeBoards;
	private List<BoardCategory> categories;
}
