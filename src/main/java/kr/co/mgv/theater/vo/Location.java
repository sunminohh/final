package kr.co.mgv.theater.vo;

import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Builder;
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
	
	@Builder
	public Location(int no, String name, List<Theater> theaters) {
		super();
		this.no = no;
		this.name = name;
		this.theaters = theaters;
	}
	
	
}
