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
public class SBoardComment {

	private int no;
	private String content;
	private Date createDate;
	private Date updateDate;
	private User user;
	private StoreBoard board;
	private SBoardComment parent;
	private SBoardComment great;
	
	@Builder
	public SBoardComment(int no, String content, Date createDate, Date updateDate, User user, StoreBoard board,
			SBoardComment parent, SBoardComment great) {
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
