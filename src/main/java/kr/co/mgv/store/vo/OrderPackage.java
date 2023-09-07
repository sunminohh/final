package kr.co.mgv.store.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Alias("OrderPackage")
public class OrderPackage {

    private int no;
    private String orderId;
    private int packageNo;
    private int amount;
    private int price;
    private int catNo;
}
