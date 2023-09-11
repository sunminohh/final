package kr.co.mgv.user.dto;

import kr.co.mgv.store.vo.Order;
import kr.co.mgv.user.vo.UserPagination;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderList {
    private UserPagination pagination;
    private List<Order> orderList;
}
