package kr.co.mgv.admin.sales.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Alias("totalSales")
public class TotalSales {

	private Integer id;
	private String name;
	private String date;
	private Integer totalSales;
}
