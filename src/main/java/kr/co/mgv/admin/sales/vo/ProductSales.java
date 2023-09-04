package kr.co.mgv.admin.sales.vo;

import org.apache.ibatis.type.Alias;

import kr.co.mgv.store.vo.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Alias("productSales")
public class ProductSales {

	private Integer id;
	private String date;
	private Product product;
	private Integer amount;
	private Integer totalSales;
}
