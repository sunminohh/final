package kr.co.mgv.board.vo;

import kr.co.mgv.user.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SBoardLike {

	private int no;
	private User user;
	private StoreBoard board;
	private String cancel;
	
	@Builder
	public SBoardLike(int no, User user, StoreBoard board, String cancel) {
		super();
		this.no = no;
		this.user = user;
		this.board = board;
		this.cancel = cancel;
	}
	
	
}
