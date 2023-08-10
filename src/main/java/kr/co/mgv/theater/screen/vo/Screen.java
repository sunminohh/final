package kr.co.mgv.theater.screen.vo;

import org.apache.ibatis.type.Alias;

import kr.co.mgv.theater.vo.Theater;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Alias("Screen")
public class Screen {
	private int id;
	private int seats;
	private String name;
	private Theater theater;
}
