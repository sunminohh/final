package kr.co.mgv.store.vo;

import kr.co.mgv.user.vo.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class OrderForm {

    private int totalPrice;
    private List<OrderData> requestData;
}
