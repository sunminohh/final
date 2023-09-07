package kr.co.mgv.store.service;

import kr.co.mgv.store.mapper.GiftTicketMapper;
import kr.co.mgv.store.mapper.OrderItemMapper;
import kr.co.mgv.store.mapper.OrderMapper;
import kr.co.mgv.store.vo.*;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final GiftTicketMapper giftTicketMapper;
    public void insertOrder(Order order) {
        orderMapper.insertOrder(order);
    }
    public void generateGiftTickets(String userId, int quantity){
        if(quantity==0){
            return;
        }

        long giftTicketNo;

        Map<String,Object> params= new HashMap<>();
        params.put("userId",userId);
        List<Object> nos= new ArrayList<>();
        for(int i=0; i<quantity; i++){
            giftTicketNo=(long)(Math.random()*10);
            for(int j=0; j<15; j++){
                giftTicketNo*=10;
                giftTicketNo+=(long)(Math.random()*10);
            }
            nos.add(giftTicketNo);
        }
        params.put("nos",nos);
        giftTicketMapper.insertGiftTickets(params);

    }
    public void insertOrderItem(OrderItem orderItem) {
        orderItemMapper.insertOrderItem(orderItem);
    }
}
