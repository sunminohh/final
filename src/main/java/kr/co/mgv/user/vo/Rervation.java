package kr.co.mgv.user.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@NoArgsConstructor
@Alias("Rervation")
public class Rervation {
    private int no;
    private Date reserveDate;
    private Date cancelDate;


}
