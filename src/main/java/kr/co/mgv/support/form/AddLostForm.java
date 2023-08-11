package kr.co.mgv.support.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddLostForm {

	private Integer locationNo;
	private Integer theaterNo;
	private String guestName;
	private String guestEmail;
	private Integer guestPassword;
	private String title;
	private String content;
	private MultipartFile upFile;
	
}

