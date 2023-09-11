package kr.co.mgv.store.controller;

import kr.co.mgv.store.service.OrderService;
import kr.co.mgv.store.vo.Order;
import kr.co.mgv.user.vo.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
@Slf4j
public class OrderRestController {

    private OrderService orderService;
    @PostMapping("/requestPayment")
    public Order requestPayment(@RequestBody Order order, @AuthenticationPrincipal User user){

        order.setUserId(user.getId());
        order.setUserName(user.getName());

        log.info("order - {}", order);
        orderService.insertOrder(order);
        return order;
    }
    @GetMapping("/orderList")
    public List<Order> getOrderList(){
    	return orderService.getOrderList();
    }
}
