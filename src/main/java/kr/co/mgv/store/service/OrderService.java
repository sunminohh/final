package kr.co.mgv.store.service;

import kr.co.mgv.store.mapper.OrderItemMapper;
import kr.co.mgv.store.mapper.OrderMapper;
import kr.co.mgv.store.vo.*;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    public void order(OrderForm orderForm, User user) {
        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(orderForm.getTotalPrice());

        orderMapper.insertOrder(order);

        int orderNo = order.getNo();

        for (OrderData orderData : orderForm.getRequestData()) {
            OrderItem orderItem = new OrderItem();
            try {
                orderItem.setOrderNo(orderNo);
                orderItem.setProductNo(orderData.getProductNo() != null ? Integer.parseInt(orderData.getProductNo()) : 0);
                orderItem.setProductAmount(orderData.getProductAmount() != null ? Integer.parseInt(orderData.getProductAmount()) : 0);
                orderItem.setProductPrice(orderData.getProductPrice() != null ? Integer.parseInt(orderData.getProductPrice()) : 0);
                orderItem.setPackageNo(orderData.getPackageNo() != null ? Integer.parseInt(orderData.getPackageNo()) : 0);
                orderItem.setPackageAmount(orderData.getPackageAmount() != null ? Integer.parseInt(orderData.getPackageAmount()) : 0);
                orderItem.setPackagePrice(orderData.getPackagePrice() != null ? Integer.parseInt(orderData.getPackagePrice()) : 0);

                log.info("주문번호zz: " + orderNo);

                orderItemMapper.insertOrderItem(orderItem);
            } catch (NumberFormatException e) {
                log.error("숫자로 변환할 수 없는 데이터: " + orderData.toString());
            }
        }
    }
}
