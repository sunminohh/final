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

	private int no;
	private String name;
	private int price;
	private int stock;
	private String soldOut;
	private String description;
	private Date createDate;
	private Date updateDate;
	private int discount;
	private int catNo;
	private String imagePath;
}
