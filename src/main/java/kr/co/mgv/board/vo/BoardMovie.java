package kr.co.mgv.board.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardMovie {

	private int no;
	private String title;
	@Builder
	public BoardMovie(int no, String title) {
		super();
		this.no = no;
		this.title = title;
	}
}
