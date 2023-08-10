package kr.co.mgv.theater.floors;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Alias("Floorinfo")
public class FloorInfo {

	private int theaterNo;
	private String id;
	private String floor;
	private String info;
}
