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
@Alias("OrderItem")
public class OrderItem {
    private int no;
    private int productNo;
    private int productAmount;
    private int productPrice;
    private int packageNo;
    private int packageAmount;
    private int packagePrice;
    private int orderNo;
}
