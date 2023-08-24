package kr.co.mgv.support.form;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
public class AddNoticeForm {

	
	private Integer locationNo;
	private Integer theaterNo;
	private Integer categoryNo;
	private String noticeType;
	private String title;
	private String content;
	
}
