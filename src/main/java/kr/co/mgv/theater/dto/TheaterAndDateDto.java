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

	public TheaterAndDateDto(int theaterNo, String date) {
		this.theaterNo = theaterNo;
		this.date = date;
	}
}
