package kr.co.mgv.support.vo;

import java.util.Date;

import kr.co.mgv.user.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OneComment {

	private int no;
	private String content;
	private String deletd;
	private Date updateDate;
	private Date createDate;
	private One one;
	private User user;
	
	@Builder
	public OneComment(int no, String content, String deletd, Date updateDate, Date createDate, One one, User user) {
		super();
		this.no = no;
		this.content = content;
		this.deletd = deletd;
		this.updateDate = updateDate;
		this.createDate = createDate;
		this.one = one;
		this.user = user;
	} 
	
}
