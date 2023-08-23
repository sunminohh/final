package kr.co.mgv.support.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import kr.co.mgv.user.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Faq {

	private int no;
	private String title;
	private String content;
	private String deleted;
	@JsonFormat(pattern = "yyyy.MM.dd")
	private Date updateDate;
	@JsonFormat(pattern = "yyyy.MM.dd")
	private Date createDate;
	private SupportCategory category;
	private User user;
	private Integer orderNo;
	
	@Builder
	public Faq(int no, String title, String content, String deleted, Date updateDate, Date createDate,
			SupportCategory category, User user, Integer orderNo) {
		super();
		this.no = no;
		this.title = title;
		this.content = content;
		this.deleted = deleted;
		this.updateDate = updateDate;
		this.createDate = createDate;
		this.category = category;
		this.user = user;
		this.orderNo = orderNo;
	}
	
	
	
}
