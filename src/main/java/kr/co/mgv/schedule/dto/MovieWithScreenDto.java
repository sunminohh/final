package kr.co.mgv.schedule.dto;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Alias("MovieWithScreenDto")
public class MovieWithScreenDto {

	private int movieNo;
	private String movieTitle;
	private int runtime;
	private String contentRating;
	private List<ScreenWithScheduleDto> screenList;
}
