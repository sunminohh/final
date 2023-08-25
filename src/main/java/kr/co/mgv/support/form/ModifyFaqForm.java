package kr.co.mgv.support.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ModifyFaqForm {

	private String title;
	private String content;
	private Integer orderNo;
	private Integer categoryNo;
		
}

