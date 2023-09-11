package kr.co.mgv.support.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OneFile {

	private int no;
	private One one;
	private String originalName;
	private String saveName;
	private String uploadPath;
	private Date createDate;
	
	@Builder
	public OneFile(int no, One one, String originalName, String saveName, String uploadPath, Date createDate) {
		super();
		this.no = no;
		this.one = one;
		this.originalName = originalName;
		this.saveName = saveName;
		this.createDate = createDate;
		this.uploadPath = uploadPath;
	}
	
	
	
}
