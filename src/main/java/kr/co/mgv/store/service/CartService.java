package kr.co.mgv.store.service;

import kr.co.mgv.store.mapper.CartMapper;
import kr.co.mgv.store.vo.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMapper cartMapper;

    public void insertCart(Cart cart) {
        cartMapper.insertCart(cart);
    }
}
