package kr.co.mgv.store.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Alias("OrderProduct")
public class OrderProduct {

    private int no;
    private String orderId;
    private int productNo;
    private int amount;
    private int price;
    private int catNo;
}
