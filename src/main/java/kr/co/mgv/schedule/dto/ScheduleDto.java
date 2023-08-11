package kr.co.mgv.schedule.dto;


import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Alias("ScheduleDto")
public class ScheduleDto {

	private int id;
	private String start;
	private String end;
	private int turn;
	private int remainingSeats;
}
