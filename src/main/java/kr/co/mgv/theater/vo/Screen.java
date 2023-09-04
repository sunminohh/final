package kr.co.mgv.theater.vo;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Alias("Screen")
public class Screen {
	private Integer id;
	private Integer seats;
	private String name;
	private Theater theater;
	private Integer screenRow;
	private Integer screenCol;
}
