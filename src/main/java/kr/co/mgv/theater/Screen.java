package kr.co.mgv.theater;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Alias("screen")
public class Screen {
	private int id;
	private int seats;
	private String name;
	private Theater theater;
}
