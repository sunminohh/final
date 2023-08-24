package kr.co.mgv.schedule.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@NoArgsConstructor
@Alias("DailyScheduleDto")
public class DailyScheduleDto {
    private int movieNo;
    private int theaterNo;
}
