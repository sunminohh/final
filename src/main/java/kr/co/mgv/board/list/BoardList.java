package kr.co.mgv.board.list;

import java.util.Date;

import kr.co.mgv.board.vo.MovieBoard;
import kr.co.mgv.board.vo.PartyBoard;
import kr.co.mgv.board.vo.StoreBoard;
import kr.co.mgv.board.vo.TheaterBoard;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardList {

	private int no;
	private Date createDate;
	private String name;
	private int readCount;
	private String type;
	@Builder
	public BoardList(int no, Date createDate, String name, int readCount, String type) {
		super();
		this.no = no;
		this.createDate = createDate;
		this.name = name;
		this.readCount = readCount;
		this.type = type;
	}
}
