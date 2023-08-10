package kr.co.mgv.board.mboard;

import kr.co.mgv.user.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MBoardLike {

	private int no;
	private User user;
	private MovieBoard board;
	
	@Builder
	public MBoardLike(int no, User user, MovieBoard board) {
		super();
		this.no = no;
		this.user = user;
		this.board = board;
	}
	
	
}
