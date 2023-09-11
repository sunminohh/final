package kr.co.mgv.admin.sales.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Alias("salesDto")
public class SalesDTO {

	private String period;
	private String firstDate;
	private String lastDate;
	private String category;
	private String targetDate;
}
