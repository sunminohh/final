package kr.co.mgv.support.form;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddFaqForm {

	private Integer categoryNo;
	private String title;
	private String content;
	
}
