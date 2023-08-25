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
public class PartyJoin {

	private int no;
	private PartyBoard board;
	private User user;
	private String request;
	private Date createDate;
	private Date updateDate;
	private String accept;

	@Builder
	public PartyJoin(int no, PartyBoard board, User user, String request, Date createDate, Date updateDate, String accept) {
		super();
		this.no = no;
		this.board = board;
		this.user = user;
		this.request = request;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.accept = accept;
	}
}
