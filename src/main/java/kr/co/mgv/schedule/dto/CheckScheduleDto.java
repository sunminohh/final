package kr.co.mgv.schedule.dto;

import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Alias("CheckScheduleDto")
public class CheckScheduleDto {

	private int theaterNo;
	private List<String> dateList;
}
