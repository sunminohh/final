package kr.co.mgv.board.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardLocation {

	private int no;
	private String name;
	
	@Builder
	public BoardLocation(int no, String name) {
		super();
		this.no = no;
		this.name = name;
	}
}
