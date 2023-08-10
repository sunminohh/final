package kr.co.mgv.theater.facility.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
