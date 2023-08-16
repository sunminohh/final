package kr.co.mgv.theater.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Alias("TheaterAndDateDto")
public class TheaterAndDateDto {
	
	private int theaterNo;
	private String date;
	private String time;
	public TheaterAndDateDto(int theaterNo, String date, String time) {
		this.theaterNo = theaterNo;
		this.date = date;
		this.time = time;
	}
}
