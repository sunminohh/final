package kr.co.mgv.theater.vo;

import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Alias("location")
public class Location {

	private int no;
	private String name;
	private List<Theater> theaters;
	
	public Location(int no) {
		this.no = no;
	}
}
