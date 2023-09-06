package kr.co.mgv.booking.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Alias("Booking")
public class Booking {
    private long           no;
    private String        userId;
    private String        userName;
    private Date          bookingDate;
    private int           movieNo;
    private String        title;
    private String        poster;
    private String        contentRating;
    private String        contentRatingKr;
    private String        startTime;
    private String        endTime;
    private int           scheduleId;
    private int           screenId;
    private String        screenName;
    private int           theaterNo;
    private String        theaterName;
    private int           totalSeats;
    private String        bookedSeatsNos;
    private int           adultSeats;
    private int           underageSeats;
    private int           giftAmount;
    private int           payAmount;
    private int           totalPrice;
    private String        payMethod;
    private Date          createDate;
    private Date          updateDate;
    private String        bookingState;
}
