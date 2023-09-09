package kr.co.mgv.store.vo;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Alias("Product")
public class Product {

	private Integer no;
	private String name;
	private Integer originalPrice;
	private Integer stock;
	private String soldOut;
	private String description;
	private Date createDate;
	private Date updateDate;
	private Integer discountedPrice;
	private Category category;
	private String imagePath;
	private String packageInfo;
	public Product(int no) {
		this.no = no;
	}
}
