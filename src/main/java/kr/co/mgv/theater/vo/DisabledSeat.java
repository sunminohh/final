package kr.co.mgv.theater.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter @Setter
@NoArgsConstructor
@Alias("DisabledSeat")@ToString
public class DisabledSeat {
    private String SeatNo;
    private int ScreenId;
}
