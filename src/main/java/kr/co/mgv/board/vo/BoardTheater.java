package kr.co.mgv.board.vo;

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
}
