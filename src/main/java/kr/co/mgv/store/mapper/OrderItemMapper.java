package kr.co.mgv.store.mapper;

import kr.co.mgv.store.vo.OrderItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemMapper {
    void insertOrderItem(OrderItem orderItem);
}
