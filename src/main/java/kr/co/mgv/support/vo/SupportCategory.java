package kr.co.mgv.support.vo;

import lombok.Builder;
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
	
	@Builder
	public SupportCategory(int no, String name, String type) {
		super();
		this.no = no;
		this.name = name;
		this.type = type;
	}
	
	
	
}
