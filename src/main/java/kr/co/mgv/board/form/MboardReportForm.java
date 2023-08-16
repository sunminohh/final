package kr.co.mgv.board.form;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MboardReportForm {

	private String reasonContent; // 신고이유 내용
	private int reasonNo;
	private int boardNo;
	
	@Builder
	public MboardReportForm(String reasonContent, int reasonNo, int boardNo) {
		super();
		this.reasonContent = reasonContent;
		this.reasonNo = reasonNo;
		this.boardNo = boardNo;
	}
}
