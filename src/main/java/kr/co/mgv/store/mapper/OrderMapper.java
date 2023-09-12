package kr.co.mgv.store.mapper;

import kr.co.mgv.store.vo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {

    void insertOrder(Order order);

    List<Order> getOrderList();

    Order getOrderById(long id);
    void updateOrder(Order order);
    void deleteOrder(long orderId);

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

    List<Order> getOrderByUesrId(String userId);
    long updateOrderById(@Param("orderId") long orderId);

}
