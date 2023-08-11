package kr.co.mgv.schedule.dto;

import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Alias("ScreenWithScheduleDto")
public class ScreenWithScheduleDto {

	private String screenId;
	private String screenName;
	private int seats;
	private String screenInfo;
	private List<ScheduleDto> scheduleList;
}
