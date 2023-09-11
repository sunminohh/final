package kr.co.mgv.store.vo;

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


    private long orderId;
    private String userId;
    private String userName;
    private int totalPrice;
    private String orderName;
    private String orderProducts;
    private String orderSpecificProducts;
    private String payMethod;
    private String paymentKey;
    private Date createDate;
    private Date updateDate;
    private String state;


}
