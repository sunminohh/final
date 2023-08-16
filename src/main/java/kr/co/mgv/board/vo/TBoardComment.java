package kr.co.mgv.board.vo;

import java.util.Date;

import kr.co.mgv.user.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TBoardComment {

	private int no;
	private String content;
	private Date createDate;
	private Date updateDate;
	private User user;
	private TheaterBoard board;
	private TBoardComment parent;
	private TBoardComment great;
	
	@Builder
	public TBoardComment(int no, String content, Date createDate, Date updateDate, User user, TheaterBoard board,
			TBoardComment parent, TBoardComment great) {
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
