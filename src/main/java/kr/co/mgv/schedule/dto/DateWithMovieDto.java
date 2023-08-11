package kr.co.mgv.schedule.dto;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Alias("DateWithMovieDto")
public class DateWithMovieDto {

	private String date;
	private int theaterNo;
	private List<MovieWithScreenDto> movieList;
}
