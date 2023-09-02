package kr.co.mgv.store.controller;

import kr.co.mgv.store.service.OrderService;
import kr.co.mgv.store.vo.OrderForm;
import kr.co.mgv.user.vo.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public String order(@RequestBody OrderForm orderForm, @AuthenticationPrincipal User user) {

        log.info("orderForm안에 : " + orderForm);

        orderService.order(orderForm, user);

        return "redirect:success";
    }
}
