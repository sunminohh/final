package kr.co.mgv.board.mboard;

import java.util.Date;

import kr.co.mgv.user.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MBoardComment {

	private int no;
	private String content;
	private Date createDate;
	private Date updateDate;
	private User user;
	private MovieBoard board;
	private MBoardComment parent;
	private MBoardComment great;
	
	@Builder
	public MBoardComment(int no, String content, Date createDate, Date updateDate, User user, MovieBoard board,
			MBoardComment parent, MBoardComment great) {
		super();
		this.no = no;
		this.content = content;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.user = user;
		this.board = board;
		this.parent = parent;
		this.great = great;
	}
	
	
	
}
