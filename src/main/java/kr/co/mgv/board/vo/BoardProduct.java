package kr.co.mgv.board.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardProduct {

	private int no;
	private String name;
	private BoardCategory category;

	@Builder
	public BoardProduct(int no, String name, BoardCategory category) {
		super();
		this.no = no;
		this.name = name;
		this.category = category;
	}
}
