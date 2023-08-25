package kr.co.mgv.store.controller;

import kr.co.mgv.store.mapper.CartMapper;
import kr.co.mgv.store.service.CartService;
import kr.co.mgv.store.vo.Cart;
import kr.co.mgv.store.vo.Package;
import kr.co.mgv.store.vo.Product;
import kr.co.mgv.user.vo.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;
    @GetMapping({"/", ""})
    public String cart() {
        return "view/store/cart";
    }

    @PostMapping("/addPackageIntoCart")
    public String insertPackageIntoCart(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        int totalPrice = Integer.parseInt(request.getParameter("totalPrice"));
        int packageNo = Integer.parseInt(request.getParameter("packageNo"));
        int packageAmount = Integer.parseInt(request.getParameter("packageAmount"));

        Cart cart = new Cart();
        cart.setTotalPrice(totalPrice);
        cart.setUser(user);
        cart.setPkg(new Package(packageNo));
        cart.setAmount(packageAmount);

        cartService.insertCart(cart);

        return "view/store/list";
    }

    @PostMapping("/addProductIntoCart")
    public String insertProductIntoCart(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        int totalPrice = Integer.parseInt(request.getParameter("totalPrice"));
        int productNo = Integer.parseInt(request.getParameter("productNo"));
        int productAmount = Integer.parseInt(request.getParameter("productAmount"));

        Cart cart = new Cart();
        cart.setTotalPrice(totalPrice);
        cart.setUser(user);
        cart.setProduct(new Product(productNo));
        cart.setAmount(productAmount);

        cartService.insertCart(cart);

        return "view/store/list";
    }


}
