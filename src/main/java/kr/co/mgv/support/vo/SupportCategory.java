package kr.co.mgv.support.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SupportCategory {

	private int no;
	private String name;
	private String type;
	
	public SupportCategory(int no) {
		this.no = no;
	}
}
