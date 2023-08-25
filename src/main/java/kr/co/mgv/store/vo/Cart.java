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
@ToString
@NoArgsConstructor
@Alias("Cart")
public class Cart {

    private int no;
    private Product product;
    private Package pkg;
    private User user;
    private int amount;
    private int totalPrice;
    private Date insertDate;
    private String paymentStatus;
}
