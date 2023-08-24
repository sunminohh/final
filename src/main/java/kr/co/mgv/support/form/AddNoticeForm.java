package kr.co.mgv.support.form;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
public class AddNoticeForm {

	private Integer categoryNo;
	private String title;
	private String content;
	
}
