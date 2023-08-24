package kr.co.mgv.schedule.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class ScheduleRegistFormDto {
	private int locationNo;
	private int theaterNo;
	private int screenId;
	private String date;
	private int movieNo;
	private String startTime;
	private String endTime;
	private int turn;
	private String screenInfo;
	private int seats;
	
	
	public ScheduleRegistFormDto(int locationNo, int theaterNo, int screenId, String date, int movieNo,
			String startTime, String endTime, int turn, String screenInfo, int seats) {
		super();
		this.locationNo = locationNo;
		this.theaterNo = theaterNo;
		this.screenId = screenId;
		this.date = date;
		this.movieNo = movieNo;
		this.startTime = startTime;
		this.endTime = endTime;
		this.turn = turn;
		this.screenInfo = screenInfo;
		this.seats = seats;
	}
	
	
}
