package kr.co.mgv.store.mapper;

import kr.co.mgv.store.vo.Cart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartMapper {
    void insertCart(Cart cart);
}
