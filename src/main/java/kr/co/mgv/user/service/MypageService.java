package kr.co.mgv.user.service;

import kr.co.mgv.store.mapper.OrderMapper;
import kr.co.mgv.store.vo.Order;
import kr.co.mgv.user.vo.UserPagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MypageService {
    private final OrderMapper orderDao;

    public HashMap<String, Object> getOrderByUserId(String userId, String startDate, String endDate, String state, int page) {

        int totalRows = this.getTotalRowsByUserId(userId, startDate, endDate, state);
        UserPagination pagination = new UserPagination(page, totalRows);

        List<Order> orders = orderDao.getOrders(userId, startDate, endDate, state, pagination.getBegin(), pagination.getEnd());
        log.info("begin -> {}", pagination.getBegin());
        log.info("end -> {}", pagination.getEnd());
        HashMap<String, Object> result = new HashMap<>();
        result.put("orders", orders);
        result.put("pagination", pagination);
        result.put("totalRows", totalRows);
        return result;
    }

    public int getTotalRowsByUserId(String userId, String startDate, String endDate, String state) {

        return orderDao.getTotalRowsByUserId(userId, startDate, endDate, state);
    }

    // 구매내역 취소
    public boolean cancelOrder(long orderId) {
        long updateRows = orderDao.updateOrderById(orderId);

        return updateRows > 0;
    }
}
