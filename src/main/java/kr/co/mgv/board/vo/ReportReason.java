package kr.co.mgv.board.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportReason {

	private int no;
	private String name;
	
	@Builder
	public ReportReason(int no, String name) {
		super();
		this.no = no;
		this.name = name;
	}
}
