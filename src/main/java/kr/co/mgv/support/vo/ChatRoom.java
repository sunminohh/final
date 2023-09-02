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
public class ChatRoom {

	private String id;
	private User admin;
	private User user;
	private Date createTime;
	
	@Builder
	public ChatRoom(String id, User admin, User user, Date createTime) {
		super();
		this.id = id;
		this.admin = admin;
		this.user = user;
		this.createTime = createTime;
	}
	
	
}
