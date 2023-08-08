package kr.co.mgv.theater.location;

import java.util.List;

import org.apache.ibatis.type.Alias;

import kr.co.mgv.theater.Theater;
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
}
