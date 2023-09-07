package kr.co.mgv.store.mapper;

import kr.co.mgv.store.vo.OrderProduct;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderProductMapper {

    void insertOrderProduct(OrderProduct orderProduct);
}
