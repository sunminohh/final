package kr.co.mgv.board.vo;

import kr.co.mgv.user.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MboardReport {

	private int no;
	private String reasonContent; // 신고이유 내용
	private ReportReason reason;
	private MovieBoard board;
	private User user;
	
	@Builder
	public MboardReport(int no, String reasonContent, ReportReason reason, MovieBoard board, User user) {
		super();
		this.no = no;
		this.reasonContent = reasonContent;
		this.reason = reason;
		this.board = board;
		this.user = user;
	}
}
