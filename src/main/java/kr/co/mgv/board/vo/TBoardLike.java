package kr.co.mgv.board.vo;

import kr.co.mgv.user.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TBoardLike {

	private int no;
	private User user;
	private TheaterBoard board;
	private String cancel;
	
	@Builder
	public TBoardLike(int no, User user, TheaterBoard board, String cancel) {
		super();
		this.no = no;
		this.user = user;
		this.board = board;
		this.cancel = cancel;
	}
	
	
}
