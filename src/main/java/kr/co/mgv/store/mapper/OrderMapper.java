package kr.co.mgv.store.mapper;

import kr.co.mgv.store.vo.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    void insertOrder(Order order);
}