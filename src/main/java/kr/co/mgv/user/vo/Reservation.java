package kr.co.mgv.user.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@NoArgsConstructor
@Alias("Reservation")
public class Reservation {
    private int no;
    private Date purchaseDate;
    private Date cancelDate;


}
