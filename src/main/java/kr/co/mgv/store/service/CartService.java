package kr.co.mgv.store.service;

import kr.co.mgv.store.mapper.CartMapper;
import kr.co.mgv.store.vo.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMapper cartMapper;

    public void insertCart(Cart cart) {
        cartMapper.insertCart(cart);
    }

    public List<Cart> getCartItemsByUserId(String userId) {
        return cartMapper.getCartItemsByUserId(userId);
    }

    public void updateCartItem(Cart cart) {
        cartMapper.updateCartItem(cart);
    }

    public void deleteCart(int cartNo) {
        cartMapper.deleteCart(cartNo);
    }
}
