package kr.co.mgv.board.form;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddSboardForm {

	private String name;
	private String content;
	private int catNo;
	private int productNo;
	private String fileName;
	
	@Builder
	public AddSboardForm(String name, String content, int catNo, int productNo, String fileName) {
		super();
		this.name = name;
		this.content = content;
		this.catNo = catNo;
		this.productNo = productNo;
		this.fileName = fileName;
	}

}
