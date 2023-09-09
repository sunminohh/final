package kr.co.mgv.store.service;

import kr.co.mgv.store.mapper.*;
import kr.co.mgv.store.vo.*;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderProductMapper orderProductMapper;
    private final OrderPackageMapper orderPackageMapper;
    private final GiftTicketMapper giftTicketMapper;

    public void insertOrder(String orderId, int amount, User user) {

        Order order = new Order();

        order.setTotalPrice(amount);

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
            giftTicketNo=(long)Math.max((Math.random()*10),1);
            for(int j=0; j<15; j++){
                giftTicketNo*=10;
                giftTicketNo+=(long)Math.max((Math.random()*10),1);
            }
            nos.add(giftTicketNo);
        }
        params.put("nos",nos);
        giftTicketMapper.insertGiftTickets(params);

    }

    public void insertOrderProduct(String orderId, int productNo, int productAmount, int productPrice, int catNo) {
        OrderProduct orderProduct = new OrderProduct();

        orderProduct.setOrderId(orderId);
        orderProduct.setProductNo(productNo);
        orderProduct.setAmount(productAmount);
        orderProduct.setPrice(productPrice);
        orderProduct.setCatNo(catNo);

        orderProductMapper.insertOrderProduct(orderProduct);
    }

    public void insertOrderPackage(String orderId, int packageNo, int packageAmount, int packagePrice, int catNo) {
        OrderPackage orderPackage = new OrderPackage();

        orderPackage.setOrderId(orderId);
        orderPackage.setPackageNo(packageNo);
        orderPackage.setAmount(packageAmount);
        orderPackage.setPrice(packagePrice);
        orderPackage.setCatNo(catNo);

        orderPackageMapper.insertOrderPackage(orderPackage);
    }
}
