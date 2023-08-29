package kr.co.mgv.store.mapper;

import kr.co.mgv.store.vo.Cart;
import kr.co.mgv.user.vo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartMapper {
    void insertCart(Cart cart);

    List<Cart> getCartItemsByUserId(String userId);

    void updateCartItem(Cart cart);
}
