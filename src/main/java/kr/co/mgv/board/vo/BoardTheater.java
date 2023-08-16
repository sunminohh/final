package kr.co.mgv.board.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BoardTheater {

	private int no;
	private String name;
	private BoardLocation location;
	
	@Builder
	public BoardTheater(int no, String name, BoardLocation location) {
		super();
		this.no = no;
		this.name = name;
		this.location = location;
	}
	
	
}
