package kr.co.mgv.schedule.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@NoArgsConstructor
@Alias("BookingScheduleDto")
public class BookingScheduleDto {
    private String id;
    private int movieNo;
    private String movieTitle;
    private int screenId;
    private String screenName;
    private int theaterNo;
    private String theaterName;
    private String startTime;
    private String endTime;
    private int turn;
    private int remainingSeats;
    private int screenSeats;
}
