package kr.co.mgv.store.vo;

import kr.co.mgv.user.vo.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@ToString
@Alias("Order")
public class Order {

    private int no;
    private User user;
    private int totalPrice;
    private Date orderDate;
    private Date cancelDate;
    private String status;
}