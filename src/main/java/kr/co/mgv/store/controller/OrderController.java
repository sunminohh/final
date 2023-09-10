package kr.co.mgv.store.controller;

import kr.co.mgv.booking.vo.Booking;
import kr.co.mgv.store.service.CartService;
import kr.co.mgv.store.service.OrderService;
import kr.co.mgv.store.vo.*;
import kr.co.mgv.user.vo.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RequestMapping("/order")
@AllArgsConstructor
@Slf4j
public class OrderController {

    private final CartService cartService;
    private final OrderService orderService;

    @ResponseBody
    @GetMapping("/requestPayment")
    public Map<String,String> payRequest(@RequestBody Order order,@AuthenticationPrincipal User user){
      Map<String,String> map = new HashMap<>();

        if(user!=null){
          map.put("result","success");
            map.put("userId",user.getId());
          map.put("userName",user.getName());
          return map;
      }
        map.put("result","fail");
        return map;
    }



    @GetMapping({"/success"})
    public String success(@RequestParam("orderId") long orderId, @RequestParam(required = false, value="amount") Long amount, @RequestParam(required = false, value="paymentKey") String paymentKey, Model model) {
        Order order= orderService.getOrderById(orderId);


        String testSK="test_sk_E92LAa5PVbpv4lOOMGJ87YmpXyJj:";
        RestTemplate rest= new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String auth= new String(Base64.getEncoder().encode(testSK.getBytes(StandardCharsets.UTF_8)));
        headers.setBasicAuth(auth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        JSONObject params= new JSONObject();
        params.put("orderId",orderId);
        params.put("amount",amount+"");
        HttpEntity entity= new HttpEntity<>(params,headers);
        log.info("토스페이먼츠 api 응답 -> {}",entity);
        ResponseEntity<String> result=rest.postForEntity("https://api.tosspayments.com/v1/payments/"+paymentKey,entity,String.class);
        order.setOrderState("결제완료");
        model.addAttribute("order", order);
        orderService.updateOrder(order);
        return "view/store/success";
    }
}


