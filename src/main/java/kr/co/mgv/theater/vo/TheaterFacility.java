package kr.co.mgv.theater.vo;

import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@NoArgsConstructor
@Alias("Facility")
public class TheaterFacility {

	private int theaterNo;
	private String id;
	private String name;
	private String icon;
	private String type;

}
