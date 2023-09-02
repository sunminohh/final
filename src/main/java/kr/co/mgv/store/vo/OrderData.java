package kr.co.mgv.store.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderData {

    private String productNo;
    private String productAmount;
    private String productPrice;
    private String packageNo;
    private String packageAmount;
    private String packagePrice;

}
