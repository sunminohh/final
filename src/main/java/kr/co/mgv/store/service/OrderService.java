package kr.co.mgv.store.service;

import kr.co.mgv.store.mapper.OrderItemMapper;
import kr.co.mgv.store.mapper.OrderMapper;
import kr.co.mgv.store.vo.*;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    public void insertOrder(Order order) {
        orderMapper.insertOrder(order);
    }

    public void insertOrderItem(OrderItem orderItem) {
        orderItemMapper.insertOrderItem(orderItem);
    }
}
