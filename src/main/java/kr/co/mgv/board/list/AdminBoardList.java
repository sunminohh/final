package kr.co.mgv.board.list;

import java.util.List;

import kr.co.mgv.board.vo.BoardReport;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminBoardList {
	
	private BoardList list;
	private List<BoardReport> reports;
	@Builder
	public AdminBoardList(BoardList list, List<BoardReport> reports) {
		super();
		this.list = list;
		this.reports = reports;
	}
}
