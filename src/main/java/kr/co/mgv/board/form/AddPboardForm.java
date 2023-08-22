package kr.co.mgv.board.form;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AddPboardForm {

	private String name;
	private String content;
	private int headCount;
	private int gender;
	private int scheduleId;
	
	@Builder
	public AddPboardForm(String name, String content, int headCount, int gender, int scheduleId) {
		super();
		this.name = name;
		this.content = content;
		this.headCount = headCount;
		this.gender = gender;
		this.scheduleId = scheduleId;
	}
}
