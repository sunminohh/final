package kr.co.mgv.schedule.dto;

import java.util.List;

import org.apache.ibatis.type.Alias;

import kr.co.mgv.theater.vo.Theater;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Alias("TheaterWithDateDto")
public class TheaterWithDateDto {

	private Theater theater;
	private List<DateWithMovieDto> dateList;
}
