package kr.co.mgv.store.controller;

import kr.co.mgv.store.service.CartService;
import kr.co.mgv.store.service.ProductService;
import kr.co.mgv.store.vo.Cart;
import kr.co.mgv.store.vo.Package;
import kr.co.mgv.store.vo.Product;
import kr.co.mgv.user.vo.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
@AllArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;
    private final ProductService productService;
    @GetMapping({"/", ""})
    public String cart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<Cart> carts = cartService.getCartItemsByUserId(user.getId());


        int totalOriginalPrice = 0;
        int totalDiscountedPrice = 0;

        for (Cart cart : carts) {
            if(cart!=null){
                Product product=  cart.getProduct();
                product.setPackageInfo(productService.getProductByNo(product.getNo()).getPackageInfo());
            }
            totalOriginalPrice += cart.getTotalOriginalPrice();
            totalDiscountedPrice += cart.getTotalDiscountedPrice();

        }
        int discountPrice = totalOriginalPrice - totalDiscountedPrice;

        model.addAttribute("carts", carts);
        model.addAttribute("totalOriginalPrice", totalOriginalPrice);
        model.addAttribute("totalDiscountedPrice", totalDiscountedPrice);
        model.addAttribute("discountPrice", discountPrice);

        log.info("카트안에 담긴 것 : " + carts);

        return "view/store/cart";
    }

    @PostMapping("/addPackageIntoCart")
    public String insertPackageIntoCart(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        int totalDiscountedPrice = Integer.parseInt(request.getParameter("totalDiscountedPrice"));
        int totalOriginalPrice = Integer.parseInt(request.getParameter("totalOriginalPrice"));
        int packageNo = Integer.parseInt(request.getParameter("packageNo"));
        int packageAmount = Integer.parseInt(request.getParameter("packageAmount"));
        int catNo = Integer.parseInt(request.getParameter("catNo"));

        List<Cart> items = cartService.getCartItemsByUserId(user.getId());

        boolean productAlreadyInCart = false;
        Cart existingCartItem = null;

        for (Cart item : items) {
            Package pkg = item.getPkg();
            if (pkg != null && pkg.getNo() == packageNo) {
                productAlreadyInCart = true;
                existingCartItem = item;
                break;
            }
        }

        if (productAlreadyInCart) {
            // 상품이 이미 장바구니에 담겨있을 경우
            int newAmount = existingCartItem.getAmount() + packageAmount;
            int newTotalDiscountedPrice = existingCartItem.getTotalDiscountedPrice() + totalDiscountedPrice;
            int newTotalOriginalPrice = existingCartItem.getTotalOriginalPrice() + totalOriginalPrice;
            existingCartItem.setAmount(newAmount);
            existingCartItem.setTotalDiscountedPrice(newTotalDiscountedPrice);
            existingCartItem.setTotalOriginalPrice(newTotalOriginalPrice);

            cartService.updateCartItem(existingCartItem);
        } else {
            // 장바구니에 해당 번호의 상품이 담겨있지 않을 경우
            Cart cart = new Cart();
            cart.setTotalDiscountedPrice(totalDiscountedPrice);
            cart.setTotalOriginalPrice(totalOriginalPrice);
            cart.setUser(user);
            cart.setPkg(new Package(packageNo));
            cart.setAmount(packageAmount);
            cart.setCatNo(catNo);

            cartService.insertCart(cart);
        }

        return "view/store/list";
    }

    @PostMapping("/addProductIntoCart")
    public String insertProductIntoCart(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        int totalDiscountedPrice = Integer.parseInt(request.getParameter("totalDiscountedPrice"));
        int totalOriginalPrice = Integer.parseInt(request.getParameter("totalOriginalPrice"));
        int productNo = Integer.parseInt(request.getParameter("productNo"));
        int productAmount = Integer.parseInt(request.getParameter("productAmount"));
        int catNo = Integer.parseInt(request.getParameter("catNo"));

        List<Cart> items = cartService.getCartItemsByUserId(user.getId());

        boolean productAlreadyInCart = false;
        Cart existingCartItem = null;

        for (Cart item : items) {
            Product product = item.getProduct();
            if (product != null && product.getNo() == productNo) {
                productAlreadyInCart = true;
                existingCartItem = item;
                break;
            }
        }

        if (productAlreadyInCart) {
            // 상품이 이미 장바구니에 담겨있을 경우
            int newAmount = existingCartItem.getAmount() + productAmount;
            int newTotalDiscountedPrice = existingCartItem.getTotalDiscountedPrice() + totalDiscountedPrice;
            int newTotalOriginalPrice = existingCartItem.getTotalOriginalPrice() + totalOriginalPrice;
            existingCartItem.setAmount(newAmount);
            existingCartItem.setTotalDiscountedPrice(newTotalDiscountedPrice);
            existingCartItem.setTotalOriginalPrice(newTotalOriginalPrice);

            cartService.updateCartItem(existingCartItem);
        } else {
            // 장바구니에 해당 번호의 상품이 담겨있지 않을 경우
            Cart cart = new Cart();
            cart.setTotalDiscountedPrice(totalDiscountedPrice);
            cart.setTotalOriginalPrice(totalOriginalPrice);
            cart.setUser(user);
            cart.setProduct(new Product(productNo));
            cart.setAmount(productAmount);
            cart.setCatNo(catNo);

            cartService.insertCart(cart);
        }

        return "view/store/list";
    }

    @PostMapping("/delete")
    public String deleteCart(@RequestParam int cartNo) {

        cartService.deleteCart(cartNo);

        return "view/store/cart";
    }
}
