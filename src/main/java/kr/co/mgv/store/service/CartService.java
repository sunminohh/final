package kr.co.mgv.store.service;

import kr.co.mgv.store.mapper.CartMapper;
import kr.co.mgv.store.vo.Cart;
import kr.co.mgv.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
}
