package kr.co.mgv.store.controller;

import kr.co.mgv.store.mapper.OrderPackageMapper;
import kr.co.mgv.store.mapper.OrderProductMapper;
import kr.co.mgv.store.service.CartService;
import kr.co.mgv.store.service.OrderService;
import kr.co.mgv.store.service.PackageService;
import kr.co.mgv.store.service.ProductService;
import kr.co.mgv.store.vo.*;
import kr.co.mgv.store.vo.Package;
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
    private final OrderProductMapper orderProductMapper;
    private final OrderPackageMapper orderPackageMapper;

    @PostMapping("/successPackage")
    public String insertPackageOrderItem(HttpServletRequest request) {
        int packagePrice = Integer.parseInt(request.getParameter("totalDiscountedPrice"));
        int packageNo = Integer.parseInt(request.getParameter("packageNo"));
        int packageAmount = Integer.parseInt(request.getParameter("packageAmount"));
        int catNo = Integer.parseInt(request.getParameter("catNo"));
        String orderId = request.getParameter("orderId");

        orderService.insertOrderPackage(orderId, packageNo, packageAmount, packagePrice, catNo);

        return "view/store/success";
    }

    @PostMapping("/successProduct")
    public String insertProductOrderItem(HttpServletRequest request) {
        int productPrice = Integer.parseInt(request.getParameter("totalDiscountedPrice"));
        int productNo = Integer.parseInt(request.getParameter("productNo"));
        int productAmount = Integer.parseInt(request.getParameter("productAmount"));
        int catNo = Integer.parseInt(request.getParameter("catNo"));
        String orderId = request.getParameter("orderId");

        orderService.insertOrderProduct(orderId, productNo, productAmount, productPrice, catNo);

        return "view/store/success";
    }

    @GetMapping("/success")
    public String paymentResult(
            Model model,
            @RequestParam(value = "orderId") String orderId,
            @RequestParam(value = "amount") Integer amount,
            @RequestParam(value = "paymentKey") String paymentKey,
            @AuthenticationPrincipal User user) throws Exception {

        List<Cart> items = cartService.getCartItemsByUserId(user.getId());

        for (Cart cart : items) {
            if (cart.getProduct() != null) {
                OrderProduct orderProduct = new OrderProduct();

                orderProduct.setOrderId(orderId);
                orderProduct.setProductNo(cart.getProduct().getNo());
                orderProduct.setAmount(cart.getAmount());
                orderProduct.setPrice(cart.getTotalDiscountedPrice());
                orderProduct.setCatNo(cart.getCatNo());

                orderProductMapper.insertOrderProduct(orderProduct);
            } else if (cart.getPkg() != null) {
                OrderPackage orderPackage = new OrderPackage();

                orderPackage.setOrderId(orderId);
                orderPackage.setPackageNo(cart.getPkg().getNo());
                orderPackage.setAmount(cart.getAmount());
                orderPackage.setPrice(cart.getTotalDiscountedPrice());
                orderPackage.setCatNo(cart.getCatNo());

                orderPackageMapper.insertOrderPackage(orderPackage);
            }
        }

        cartService.deleteCartByUserId(user.getId());

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

        orderService.insertOrder(orderId, amount, user);

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
