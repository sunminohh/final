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
public class PBoardComment {

	private int no;
	private String content;
	private Date createDate;
	private Date updateDate;
	private User user;
	private PartyBoard board;
	private PBoardComment parent;
	private PBoardComment great;
	@Builder
	public PBoardComment(int no, String content, Date createDate, Date updateDate, User user, PartyBoard board,
			PBoardComment parent, PBoardComment great) {
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
