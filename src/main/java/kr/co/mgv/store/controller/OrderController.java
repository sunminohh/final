package kr.co.mgv.store.controller;

import kr.co.mgv.store.service.CartService;
import kr.co.mgv.store.service.OrderService;
import kr.co.mgv.store.service.PackageService;
import kr.co.mgv.store.service.ProductService;
import kr.co.mgv.store.vo.Cart;
import kr.co.mgv.store.vo.Order;
import kr.co.mgv.store.vo.OrderItem;
import kr.co.mgv.store.vo.Package;
import kr.co.mgv.store.vo.Product;
import kr.co.mgv.user.vo.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/order")
@AllArgsConstructor
@Slf4j
public class OrderController {

    private final CartService cartService;
    private final OrderService orderService;

    @PostMapping("/successPackage")
    public String insertPackageOrderItem(HttpServletRequest request) {
        int totalDiscountedPrice = Integer.parseInt(request.getParameter("totalDiscountedPrice"));
        int packageNo = Integer.parseInt(request.getParameter("packageNo"));
        int packageAmount = Integer.parseInt(request.getParameter("packageAmount"));
        int catNo = Integer.parseInt(request.getParameter("catNo"));
        String orderId = request.getParameter("orderId");

        log.info("packageNo의 값 - {}", packageNo);
        log.info("orderId의 값 - {}", orderId);

        OrderItem orderItem = new OrderItem();

            orderItem.setOrderId(orderId);
            orderItem.setPackageNo(packageNo);
            orderItem.setPackageAmount(packageAmount);
            orderItem.setPackagePrice(totalDiscountedPrice);
            orderItem.setCatNo(catNo);

        orderService.insertOrderItem(orderItem);

        return "view/store/success";
    }

    @PostMapping("/successProduct")
    public String insertProductOrderItem(HttpServletRequest request) {
        int totalDiscountedPrice = Integer.parseInt(request.getParameter("totalDiscountedPrice"));
        int productNo = Integer.parseInt(request.getParameter("productNo"));
        int productAmount = Integer.parseInt(request.getParameter("productAmount"));
        int catNo = Integer.parseInt(request.getParameter("catNo"));
        String orderId = request.getParameter("orderId");

        log.info("productNo의 값 - {}", productNo);
        log.info("orderId의 값 - {}", orderId);

        OrderItem orderItem = new OrderItem();

        orderItem.setOrderId(orderId);
        orderItem.setProductNo(productNo);
        orderItem.setProductAmount(productAmount);
        orderItem.setProductPrice(totalDiscountedPrice);
        orderItem.setCatNo(catNo);

        orderService.insertOrderItem(orderItem);

        return "view/store/success";
    }

    @GetMapping("/success")
    public String paymentResult(
            Model model,
            @RequestParam(value = "orderId") String orderId,
            @RequestParam(value = "amount") Integer amount,
            @RequestParam(value = "paymentKey") String paymentKey,
            @AuthenticationPrincipal User user) throws Exception {

        Order order = new Order();

        order.setTotalPrice(amount);
        order.setId(orderId);
        order.setUser(user);

        orderService.insertOrder(order);

        List<Cart> items = cartService.getCartItemsByUserId(user.getId());

        for (Cart item : items) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            if (item.getProduct() != null) {
                orderItem.setProductNo(item.getProduct().getNo());
                orderItem.setProductAmount(item.getAmount());
                orderItem.setProductPrice(item.getTotalDiscountedPrice());
                orderItem.setCatNo(item.getCatNo());
            } else if (item.getPkg() != null) {
                orderItem.setPackageNo(item.getPkg().getNo());
                orderItem.setPackageAmount(item.getAmount());
                orderItem.setPackagePrice(item.getTotalDiscountedPrice());
                orderItem.setCatNo(item.getCatNo());
            }
            orderService.insertOrderItem(orderItem);
            cartService.deleteCart(item.getNo());
        }

        String secretKey = "test_sk_ODnyRpQWGrNkGP56g4B8Kwv1M9EN:";

        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(secretKey.getBytes("UTF-8"));
        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);

        URL url = new URL("https://api.tosspayments.com/v1/payments/" + paymentKey);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);
        obj.put("amount", amount);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        int code = connection.getResponseCode();

        boolean isSuccess = code == 200 ? true : false;
        model.addAttribute("isSuccess", isSuccess);

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();
        model.addAttribute("responseStr", jsonObject.toJSONString());
        System.out.println(jsonObject.toJSONString());

        model.addAttribute("method", (String) jsonObject.get("method"));
        model.addAttribute("orderName", (String) jsonObject.get("orderName"));
        model.addAttribute("items", items);

       String orderName = jsonObject.get("orderName").toString();
        orderService.generateGiftTickets(user.getId(),Integer.parseInt(orderName.split(" ")[0]));
        log.info("orderName - {}", orderName);

        if (((String) jsonObject.get("method")) != null) {
            if (((String) jsonObject.get("method")).equals("카드")) {
                model.addAttribute("cardNumber", (String) ((JSONObject) jsonObject.get("card")).get("number"));
            }
        } else {
            model.addAttribute("code", (String) jsonObject.get("code"));
            model.addAttribute("message", (String) jsonObject.get("message"));
        }

        return "view/store/success";
    }

    @GetMapping("/fail")
    public String paymentResult(
            Model model,
            @RequestParam(value = "message") String message,
            @RequestParam(value = "code") Integer code
    ) throws Exception {

        model.addAttribute("code", code);
        model.addAttribute("message", message);

        return "view/store/fail";
    }
}
