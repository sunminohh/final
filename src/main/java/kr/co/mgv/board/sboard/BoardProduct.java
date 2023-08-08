package kr.co.mgv.board.sboard;

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
}
