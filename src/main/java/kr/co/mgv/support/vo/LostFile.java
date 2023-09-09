package kr.co.mgv.support.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LostFile {

	private int no;
	private Lost lost;
	private String originalName;
	private String saveName;
	private String uploadPath;
	private Date createDate;

	@Builder
	public LostFile(int no, Lost lost, String originalName, String saveName, String uploadPath, Date createDate) {
		super();
		this.no = no;
		this.lost = lost;
		this.originalName = originalName;
		this.saveName = saveName;
		this.uploadPath = uploadPath;
		this.createDate = createDate;
	}
	
	
	
}
