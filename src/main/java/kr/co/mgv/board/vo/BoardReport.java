package kr.co.mgv.board.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardReport {

	private String reason;
	private String reasonName;
	private int boardNo;
	@Builder
	public BoardReport(String reason, String reasonName, int boardNo) {
		super();
		this.reason = reason;
		this.reasonName = reasonName;
		this.boardNo = boardNo;
	}
}
