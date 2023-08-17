package kr.co.mgv.board.form;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddTboardForm {

	private String name;
	private String content;
	private int locationNo;
	private int theaterNo;
	private String fileName;
	
	@Builder
	public AddTboardForm(String name, String content, int locationNo, int theaterNo, String fileName) {
		super();
		this.name = name;
		this.content = content;
		this.locationNo = locationNo;
		this.theaterNo = theaterNo;
		this.fileName = fileName;
	}
	
}
