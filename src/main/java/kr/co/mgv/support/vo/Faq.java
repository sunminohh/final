package kr.co.mgv.support.vo;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Faq {

	private int no;
	private String title;
	private String Content;
	private String deleted;
	private Date updateDate;
	private Date createDate;
	private SupportCategory category; 
	
}
