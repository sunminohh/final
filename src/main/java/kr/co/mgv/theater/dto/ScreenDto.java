package kr.co.mgv.theater.dto;

import java.util.List;

import org.apache.ibatis.type.Alias;

import kr.co.mgv.theater.vo.Screen;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ScreenDto {

	private Screen screen;
	private List<String> disabledSeats;
}
