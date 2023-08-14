package kr.co.mgv.board.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddMboardForm {

	private String name;
	private String content;
	private int movieNo;
	private String fileName;
	
	@Builder
	public AddMboardForm(String name, String content, int movieNo, String userId, String fileName) {
		super();
		this.name = name;
		this.content = content;
		this.movieNo = movieNo;
		this.fileName = fileName;
	}
	
	
}
