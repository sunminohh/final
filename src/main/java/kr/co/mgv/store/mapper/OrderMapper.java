package kr.co.mgv.store.mapper;

import kr.co.mgv.store.vo.Order;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {

    void insertOrder(Order order);

    List<Order> getOrderList();

    List<Order> getOrders(@Param("userId") String userId,
                          @Param("startDate") String startDate,
                          @Param("endDate") String endDate,
                          @Param("state") String state,
                          @Param("begin") int begin,
                          @Param("end") int end);

    int getTotalRowsByUserId(@Param("userId") String userId,
                             @Param("startDate") String startDate,
                             @Param("endDate") String endDate,
                             @Param("state") String state);

    int updateOrderById(@Param("orderId") long orderId);

    void updateOrder(Order order);

    Order getOrderById(long id);
}
